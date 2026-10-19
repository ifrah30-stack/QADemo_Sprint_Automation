
package cart;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

import baseClass.BaseClass;

public class CartTest extends BaseClass {

    @Test(groups = {"FT", "INT", "Pos"})
    public void cartPageTest() {

        driver.get("https://qademo.com/");

        wait.until(ExpectedConditions.elementToBeClickable(
            By.linkText("Products")
        )).click();

        wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//button[contains(.,'Add')]")
        ));

        wait.until(ExpectedConditions.elementToBeClickable(
            By.cssSelector("a[href='/cart']")
        )).click();

        try {
            wait.until(ExpectedConditions.urlContains("/cart"));
        } catch (Exception e) {
            System.out.println("Current URL: " + driver.getCurrentUrl());
            System.out.println(
                "Page text: "
                + driver.findElement(By.tagName("body")).getText()
            );
            throw e;
        }

        String pageText =
            driver.findElement(By.tagName("body")).getText();

        System.out.println("Cart page opened successfully.");

        Assert.assertTrue(
            driver.getCurrentUrl().contains("/cart"),
            "Shopping Cart URL was not opened."
        );

        Assert.assertTrue(
            pageText.contains("Cart"),
            "Cart page content was not displayed."
        );
    }
}
