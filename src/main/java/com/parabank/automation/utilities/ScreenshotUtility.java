package com.parabank.automation.utilities;

import com.parabank.automation.constants.FrameworkConstants;
import com.parabank.automation.driver.DriverFactory;
import com.parabank.automation.logger.Log;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public final class ScreenshotUtility {

    private ScreenshotUtility() {
    }

    public static String captureScreenshot(String testName) {
        WebDriver driver = DriverFactory.getDriver();
        if (!(driver instanceof TakesScreenshot)) {
            Log.warn(ScreenshotUtility.class, "Driver does not support screenshots");
            return null;
        }

        try {
            FileUtility.createDirectoryIfNotExists(FrameworkConstants.SCREENSHOTS_DIR);
            String fileName = testName.replaceAll("[^a-zA-Z0-9_-]", "_")
                    + "_" + DateUtility.getCurrentTimestamp() + ".png";
            Path destination = Paths.get(FrameworkConstants.SCREENSHOTS_DIR, fileName);

            Path source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE).toPath();
            Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
            Log.info(ScreenshotUtility.class, "Screenshot saved: " + destination.toAbsolutePath());
            return destination.toAbsolutePath().toString();
        } catch (IOException e) {
            Log.error(ScreenshotUtility.class, "Failed to capture screenshot for test: " + testName, e);
            return null;
        }
    }
}
