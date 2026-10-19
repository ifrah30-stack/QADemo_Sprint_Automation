
package genericUtility;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class ScreenshotUtility {

    public static String captureScreenshot(
            WebDriver driver, String testName) {

        if (driver == null) {
            throw new IllegalArgumentException(
                    "WebDriver is null. Screenshot cannot be captured."
            );
        }

        String projectPath = System.getProperty("user.dir");
        File screenshotFolder = new File(
                projectPath + File.separator + "Screenshots"
        );

        if (!screenshotFolder.exists()
                && !screenshotFolder.mkdirs()) {
            throw new RuntimeException(
                    "Could not create folder: "
                    + screenshotFolder.getAbsolutePath()
            );
        }

        String timestamp = new SimpleDateFormat(
                "yyyyMMdd_HHmmss_SSS"
        ).format(new Date());

        String safeTestName = testName.replaceAll(
                "[^a-zA-Z0-9_-]", "_"
        );

        File destination = new File(
                screenshotFolder,
                safeTestName + "_" + timestamp + ".png"
        );

        File source = ((TakesScreenshot) driver)
                .getScreenshotAs(OutputType.FILE);

        try {
            java.nio.file.Files.copy(
                    source.toPath(),
                    destination.toPath()
            );

            System.out.println(
                    "SCREENSHOT SAVED AT: "
                    + destination.getAbsolutePath()
            );

            return destination.getAbsolutePath();

        } catch (IOException e) {
            throw new RuntimeException(
                    "Unable to save screenshot: "
                    + destination.getAbsolutePath(), e
            );
        }
    }
}
