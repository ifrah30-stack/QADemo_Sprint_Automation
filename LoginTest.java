
package login;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

import baseClass.BaseClass;
import genericUtility.ExcelUtility;

public class LoginTest extends BaseClass {

    @Test(groups = {"FT", "INT", "Pos"})
    public void validLoginTest() {

        String email = ExcelUtility.getCellData("LoginData", 1, 1);
        String password = ExcelUtility.getCellData("LoginData", 1, 2);

        driver.get("https://qademo.com/login");

        By emailLocator = By.xpath(
            "//input[contains(translate(@placeholder," +
            "'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')," +
            "'username or email')]"
        );

        By passwordLocator = By.xpath("//input[@type='password']");

        WebElement emailField = wait.until(
            ExpectedConditions.visibilityOfElementLocated(emailLocator)
        );
        emailField.clear();
        emailField.sendKeys(email);

        WebElement passwordField = wait.until(
            ExpectedConditions.visibilityOfElementLocated(passwordLocator)
        );
        passwordField.clear();
        passwordField.sendKeys(password);
        passwordField.sendKeys(Keys.ENTER);

        wait.until(ExpectedConditions.not(
            ExpectedConditions.urlContains("/login")
        ));

        Assert.assertFalse(
            driver.getCurrentUrl().contains("/login"),
            "Valid login did not succeed."
        );
    }

    @Test(groups = {"Neg"})
    public void invalidLoginTest() {

        driver.get("https://qademo.com/login");

        By emailLocator = By.xpath(
            "//input[contains(translate(@placeholder," +
            "'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')," +
            "'username or email')]"
        );

        By passwordLocator = By.xpath("//input[@type='password']");

        WebElement emailField = wait.until(
            ExpectedConditions.visibilityOfElementLocated(emailLocator)
        );
        emailField.sendKeys("invalid-user@example.com");

        WebElement passwordField = wait.until(
            ExpectedConditions.visibilityOfElementLocated(passwordLocator)
        );
        passwordField.sendKeys("WrongPassword123");
        passwordField.sendKeys(Keys.ENTER);

        wait.until(ExpectedConditions.or(
            ExpectedConditions.urlContains("/login"),
            ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[contains(translate(text()," +
                "'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')," +
                "'invalid') or contains(translate(text()," +
                "'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')," +
                "'incorrect') or contains(translate(text()," +
                "'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')," +
                "'failed')]")
            )
        ));

        Assert.assertTrue(
            driver.getCurrentUrl().contains("/login"),
            "Invalid login unexpectedly left the login page."
        );
    }
}
