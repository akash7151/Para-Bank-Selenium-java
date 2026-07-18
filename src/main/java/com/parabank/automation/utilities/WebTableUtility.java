package com.parabank.automation.utilities;

import com.parabank.automation.driver.DriverFactory;
import com.parabank.automation.enums.WaitStrategy;
import com.parabank.automation.exceptions.FrameworkException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class WebTableUtility {

    private WebTableUtility() {
    }

    public static int getRowCount(By tableLocator) {
        return getRows(tableLocator).size();
    }

    public static int getColumnCount(By tableLocator) {
        List<WebElement> rows = getRows(tableLocator);
        if (rows.isEmpty()) {
            return 0;
        }
        return rows.get(0).findElements(By.tagName("td")).size();
    }

    public static String getCellData(By tableLocator, int rowIndex, int columnIndex) {
        WebElement cell = getCellElement(tableLocator, rowIndex, columnIndex);
        return cell.getText().trim();
    }

    public static List<Map<String, String>> getTableData(By tableLocator) {
        List<WebElement> headerCells = DriverFactory.getDriver()
                .findElement(tableLocator)
                .findElements(By.cssSelector("thead th, thead td"));
        List<String> headers = headerCells.stream().map(cell -> cell.getText().trim()).toList();

        List<Map<String, String>> tableData = new ArrayList<>();
        List<WebElement> rows = getRows(tableLocator);
        for (WebElement row : rows) {
            List<WebElement> cells = row.findElements(By.tagName("td"));
            Map<String, String> rowData = new HashMap<>();
            for (int i = 0; i < cells.size() && i < headers.size(); i++) {
                rowData.put(headers.get(i), cells.get(i).getText().trim());
            }
            tableData.add(rowData);
        }
        return tableData;
    }

    public static WebElement getCellElement(By tableLocator, int rowIndex, int columnIndex) {
        List<WebElement> rows = getRows(tableLocator);
        if (rowIndex >= rows.size()) {
            throw new FrameworkException("Row index out of bounds: " + rowIndex);
        }
        List<WebElement> cells = rows.get(rowIndex).findElements(By.tagName("td"));
        if (columnIndex >= cells.size()) {
            throw new FrameworkException("Column index out of bounds: " + columnIndex);
        }
        return cells.get(columnIndex);
    }

    private static List<WebElement> getRows(By tableLocator) {
        WebDriver driver = DriverFactory.getDriver();
        WaitUtility.waitForElement(tableLocator, WaitStrategy.VISIBLE);
        return driver.findElement(tableLocator).findElements(By.cssSelector("tbody tr"));
    }
}
