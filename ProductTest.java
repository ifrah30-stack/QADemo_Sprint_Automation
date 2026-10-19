
package product;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

import baseClass.BaseClass;

public class ProductTest extends BaseClass {

    @Test(groups = {"FT", "INT", "Pos"})
    public void productCatalogTest() {

        driver.get("https://qademo.com/");

        By productsLink = By.linkText("Products");

        wait.until(
            ExpectedConditions.elementToBeClickable(productsLink)
        ).click();

        try {
            wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("/products"),
                ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//button[contains(.,'Add')]")
                )
            ));
        } catch (Exception e) {
            System.out.println(
                "Current URL: " + driver.getCurrentUrl()
            );
            System.out.println(
                "Page title: " + driver.getTitle()
            );
            System.out.println(
                "Page text: "
                + driver.findElement(By.tagName("body")).getText()
            );
            throw e;
        }

        Assert.assertTrue(
            driver.findElements(
                By.xpath("//button[contains(.,'Add')]")
            ).size() > 0,
            "Product catalog did not display any Add buttons."
        );
    }
}
