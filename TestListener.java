
package genericUtility;

import org.testng.ITestListener;
import org.testng.ITestResult;

import baseClass.BaseClass;

public class TestListener implements ITestListener {

    @Override
    public void onTestSuccess(ITestResult result) {

        String[] groups = result.getMethod().getGroups();

        for (String group : groups) {

            if (group.equalsIgnoreCase("Neg")) {

                saveScreenshot(result, "Negative");
                return;
            }
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {

        saveScreenshot(result, "Failure");
    }

    private void saveScreenshot(
            ITestResult result, String screenshotType) {

        System.out.println(
                "Screenshot listener triggered for: "
                + result.getName()
        );

        Object testClass = result.getInstance();

        if (!(testClass instanceof BaseClass)) {
            System.out.println(
                    "ERROR: Test class does not extend BaseClass."
            );
            return;
        }

        BaseClass base = (BaseClass) testClass;

        if (base.getDriver() == null) {
            System.out.println(
                    "ERROR: WebDriver is null. Screenshot not saved."
            );
            return;
        }

        try {

            String screenshotPath =
                    ScreenshotUtility.captureScreenshot(
                            base.getDriver(),
                            screenshotType + "_" + result.getName()
                    );

            System.out.println(
                    "SCREENSHOT SAVED AT: " + screenshotPath
            );

        } catch (Exception e) {

            System.out.println(
                    "ERROR SAVING SCREENSHOT: " + e.getMessage()
            );

            e.printStackTrace();
        }
    }
}
