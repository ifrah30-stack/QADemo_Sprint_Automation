
package checkout;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

import baseClass.BaseClass;

public class CheckoutTest extends BaseClass {

    @Test(groups = {"FT", "INT", "Pos"})
    public void checkoutPageTest() {

        driver.get("https://qademo.com/");

        wait.until(ExpectedConditions.elementToBeClickable(
            By.linkText("Products")
        )).click();

        wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//button[contains(.,'Add')]")
        ));

        driver.findElement(
            By.cssSelector("a[href='/cart']")
        ).click();

        wait.until(ExpectedConditions.urlContains("/cart"));

        System.out.println("Cart page opened.");

        Assert.assertTrue(
            driver.getCurrentUrl().contains("/cart"),
            "Cart page was not opened."
        );
    }
}
