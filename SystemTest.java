
package system;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

import baseClass.BaseClass;

public class SystemTest extends BaseClass {

    @Test(groups = {"SYS", "Pos"})
    public void applicationHomePageTest() {

        driver.get("https://qademo.com/");

        wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//*[contains(text(),'Browse Products')]")
        ));

        Assert.assertEquals(
            driver.getCurrentUrl(),
            "https://qademo.com/",
            "Application home page URL is incorrect."
        );

        Assert.assertTrue(
            driver.findElement(
                By.xpath("//*[contains(text(),'Browse Products')]")
            ).isDisplayed(),
            "Application home page is not displayed correctly."
        );

        System.out.println("System Test passed.");
    }
}
