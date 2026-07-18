package com.parabank.automation.utilities;

import com.parabank.automation.driver.DriverFactory;
import com.parabank.automation.exceptions.FrameworkException;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public final class WindowUtility {

    private WindowUtility() {
    }

    public static void switchToWindow(String titleOrUrlFragment) {
        WebDriver driver = DriverFactory.getDriver();
        for (String handle : driver.getWindowHandles()) {
            driver.switchTo().window(handle);
            if (driver.getTitle().contains(titleOrUrlFragment)
                    || driver.getCurrentUrl().contains(titleOrUrlFragment)) {
                return;
            }
        }
        throw new FrameworkException("Window not found containing: " + titleOrUrlFragment);
    }

    public static void switchToWindowByIndex(int index) {
        List<String> handles = new ArrayList<>(DriverFactory.getDriver().getWindowHandles());
        if (index < 0 || index >= handles.size()) {
            throw new FrameworkException("Invalid window index: " + index);
        }
        DriverFactory.getDriver().switchTo().window(handles.get(index));
    }

    public static void closeCurrentWindowAndSwitchToMain() {
        WebDriver driver = DriverFactory.getDriver();
        String mainHandle = driver.getWindowHandles().iterator().next();
        driver.close();
        driver.switchTo().window(mainHandle);
    }

    public static int getWindowCount() {
        return DriverFactory.getDriver().getWindowHandles().size();
    }

    public static Set<String> getAllWindowHandles() {
        return DriverFactory.getDriver().getWindowHandles();
    }
}
