
package ordermanagement;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import baseClass.BaseClass;
import genericUtility.ExcelUtility;
import genericUtility.TestListener;

@Listeners(TestListener.class)
public class OrderManagementTest extends BaseClass {

    private final By emailLocator = By.xpath(
        "//input[contains(translate(@placeholder," +
        "'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')," +
        "'username or email')]"
    );

    private final By passwordLocator =
        By.xpath("//input[@type='password']");

    private final By addToCartLocator =
        By.cssSelector("[data-testid^='product-add-to-cart-']");

    private final By placeOrderLocator = By.xpath(
        "//button[contains(.,'Place Order')]"
        + " | //button[contains(.,'Place order')]"
    );

    // Login using Excel test data
    private void login() {

        driver.get("https://qademo.com/login");

        String email =
            ExcelUtility.getCellData("LoginData", 1, 1);

        String password =
            ExcelUtility.getCellData("LoginData", 1, 2);

        WebElement emailField = wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                emailLocator
            )
        );

        emailField.clear();
        emailField.sendKeys(email);

        WebElement passwordField = wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                passwordLocator
            )
        );

        passwordField.clear();
        passwordField.sendKeys(password);
        passwordField.sendKeys(Keys.ENTER);

        wait.until(
            ExpectedConditions.not(
                ExpectedConditions.urlContains("/login")
            )
        );

        System.out.println("Login successful.");
    }

    // Open product catalog
    private void openProducts() {

        driver.get("https://qademo.com/catalog");

        System.out.println(
            "Current URL: " + driver.getCurrentUrl()
        );

        System.out.println(
            "Page title: " + driver.getTitle()
        );

        wait.until(
            ExpectedConditions.presenceOfAllElementsLocatedBy(
                addToCartLocator
            )
        );

        System.out.println("Product catalog opened.");
    }

    // Add first available product
    private void addProduct() {

        WebElement addButton = wait.until(
            ExpectedConditions.elementToBeClickable(
                addToCartLocator
            )
        );

        System.out.println(
            "Selected product button: " + addButton.getText()
        );

        addButton.click();

        System.out.println("Add to Cart clicked.");
    }

    // Navigate to checkout
    private void openCheckout() {

        driver.get("https://qademo.com/cart");

        By checkoutLocator = By.xpath(
            "//a[contains(.,'Checkout')]"
            + " | //button[contains(.,'Checkout')]"
        );

        WebElement checkoutButton = wait.until(
            ExpectedConditions.elementToBeClickable(
                checkoutLocator
            )
        );

        checkoutButton.click();

        wait.until(
            ExpectedConditions.urlContains("checkout")
        );

        System.out.println("Checkout page opened.");
    }

    // Find checkout field by ID
    private WebElement field(String id) {

        return wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                By.id(id)
            )
        );
    }

    // Enter valid checkout details
    private void fillCheckout() {

        field("firstName").sendKeys("Test");
        field("lastName").sendKeys("User");
        field("address").sendKeys("123 Test Street");
        field("cardNumber").sendKeys("4111111111111111");
        field("expiry").sendKeys("12/30");
        field("cvv").sendKeys("123");
        field("nameOnCard").sendKeys("Test User");

        System.out.println("Checkout details entered.");
    }

    // Scroll to and click Place Order
    private void placeOrder() {

        WebElement placeOrderButton = wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                placeOrderLocator
            )
        );

        JavascriptExecutor js =
            (JavascriptExecutor) driver;

        js.executeScript(
            "arguments[0].scrollIntoView({block:'center'});",
            placeOrderButton
        );

        wait.until(
            ExpectedConditions.elementToBeClickable(
                placeOrderLocator
            )
        );

        try {
            placeOrderButton.click();

        } catch (
            org.openqa.selenium.ElementClickInterceptedException e
        ) {

            System.out.println(
                "Normal click intercepted. Retrying with JavaScript."
            );

            placeOrderButton = driver.findElement(
                placeOrderLocator
            );

            js.executeScript(
                "arguments[0].click();",
                placeOrderButton
            );
        }

        System.out.println("Place Order click attempted.");
    }

    // Positive test: verify order confirmation
    @Test(groups = {"FT", "INT", "SYS", "Pos"})
    public void orderConfirmationTest() {

        login();
        openProducts();
        addProduct();
        openCheckout();
        fillCheckout();
        placeOrder();

        wait.until(
            ExpectedConditions.textToBePresentInElementLocated(
                By.tagName("body"),
                "Order Confirmed!"
            )
        );

        String pageText = driver.findElement(
            By.tagName("body")
        ).getText();

        System.out.println(
            "ORDER CONFIRMATION PAGE TEXT:\n" + pageText
        );

        Assert.assertTrue(
            pageText.contains("Order Confirmed!"),
            "Order confirmation message was not displayed."
        );

        Assert.assertTrue(
            pageText.contains("Order #"),
            "Order number was not displayed."
        );

        System.out.println(
            "Order confirmation and order number verified."
        );
    }

    // Positive test: verify product catalog
    @Test(groups = {"FT", "INT", "SYS", "Pos"})
    public void productCatalogNavigationTest() {

        login();
        openProducts();

        Assert.assertTrue(
            driver.findElements(addToCartLocator).size() > 0,
            "Products were not displayed in the catalog."
        );

        System.out.println(
            "Product catalog navigation test passed."
        );
    }

    // Negative test: submit checkout with missing first name
    @Test(groups = {"FT", "SYS", "Neg"})
    public void missingShippingDetailsTest() {

        login();
        openProducts();
        addProduct();
        openCheckout();

        WebElement firstNameField = field("firstName");
        firstNameField.clear();

        // Fill all other checkout fields
        field("lastName").sendKeys("User");
        field("address").sendKeys("123 Test Street");
        field("cardNumber").sendKeys("4111111111111111");
        field("expiry").sendKeys("12/30");
        field("cvv").sendKeys("123");
        field("nameOnCard").sendKeys("Test User");

        placeOrder();

        try {
            wait.until(
                ExpectedConditions.or(
                    ExpectedConditions.textToBePresentInElementLocated(
                        By.tagName("body"),
                        "Order Confirmed!"
                    ),
                    ExpectedConditions.textToBePresentInElementLocated(
                        By.tagName("body"),
                        "required"
                    ),
                    ExpectedConditions.textToBePresentInElementLocated(
                        By.tagName("body"),
                        "First name"
                    ),
                    ExpectedConditions.textToBePresentInElementLocated(
                        By.tagName("body"),
                        "first name"
                    )
                )
            );

        } catch (org.openqa.selenium.TimeoutException e) {
            System.out.println(
                "No recognized confirmation or validation text appeared."
            );
        }

        String pageText = driver.findElement(
            By.tagName("body")
        ).getText();

        String lowerPageText = pageText.toLowerCase();

        boolean orderConfirmed =
            pageText.contains("Order Confirmed!");

        boolean validationMessage =
            lowerPageText.contains("required")
            || lowerPageText.contains("first name");

        System.out.println(
            "Order confirmation displayed: " + orderConfirmed
        );

        System.out.println(
            "Validation message displayed: " + validationMessage
        );

        Assert.assertFalse(
            orderConfirmed,
            "Defect: order was confirmed despite the missing first name."
        );

        Assert.assertTrue(
            validationMessage,
            "No explicit validation message was detected for the missing first name."
        );

        System.out.println(
            "Missing shipping details test passed."
        );
    }
}
