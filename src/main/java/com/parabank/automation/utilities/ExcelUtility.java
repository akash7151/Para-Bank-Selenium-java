package com.parabank.automation.utilities;

import com.parabank.automation.constants.FrameworkConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ExcelUtility {

    private static final Logger logger = LogManager.getLogger(ExcelUtility.class);

    private ExcelUtility() {
    }

    public static List<Map<String, String>> getSheetData(String sheetName) {
        List<Map<String, String>> data = new ArrayList<>();

        try (InputStream inputStream = ExcelUtility.class.getClassLoader()
                .getResourceAsStream(FrameworkConstants.TEST_DATA_FILE);
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new RuntimeException("Sheet not found: " + sheetName);
            }

            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                return data;
            }

            int columnCount = headerRow.getLastCellNum();
            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row == null || isRowEmpty(row)) {
                    continue;
                }

                Map<String, String> rowData = new HashMap<>();
                for (int colIndex = 0; colIndex < columnCount; colIndex++) {
                    Cell headerCell = headerRow.getCell(colIndex);
                    Cell dataCell = row.getCell(colIndex);
                    if (headerCell != null) {
                        String header = getCellValue(headerCell);
                        rowData.put(header, getCellValue(dataCell));
                    }
                }
                data.add(rowData);
            }
            logger.info("Loaded {} rows from sheet '{}'", data.size(), sheetName);
        } catch (IOException e) {
            logger.error("Failed to read Excel file", e);
            throw new RuntimeException("Failed to read Excel file: " + FrameworkConstants.TEST_DATA_FILE, e);
        }

        return data;
    }

    private static boolean isRowEmpty(Row row) {
        for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i);
            if (cell != null && cell.getCellType() != CellType.BLANK
                    && !getCellValue(cell).isBlank()) {
                return false;
            }
        }
        return true;
    }

    private static String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> {
                if (DateUtil.isCellDateFormatted(cell)) {
                    yield cell.getDateCellValue().toString();
                }
                double value = cell.getNumericCellValue();
                if (value == Math.floor(value)) {
                    yield String.valueOf((long) value);
                }
                yield String.valueOf(value);
            }
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> cell.getCellFormula();
            default -> "";
        };
    }
}
