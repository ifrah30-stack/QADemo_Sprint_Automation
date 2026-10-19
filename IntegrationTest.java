
package integration;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

import baseClass.BaseClass;

public class IntegrationTest extends BaseClass {

    @Test(groups = {"INT", "Pos"})
    public void homePageToProductCatalogTest() {

        driver.get("https://qademo.com/");

        wait.until(ExpectedConditions.elementToBeClickable(
            By.linkText("Products")
        )).click();

        wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//button[contains(.,'Add')]")
        ));

        Assert.assertTrue(
            driver.findElements(
                By.xpath("//button[contains(.,'Add')]")
            ).size() > 0,
            "Product catalog did not load correctly."
        );

        System.out.println(
            "Home page to Product Catalog integration passed."
        );
    }
}
