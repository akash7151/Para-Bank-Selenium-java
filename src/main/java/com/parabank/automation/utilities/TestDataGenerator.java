package com.parabank.automation.utilities;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * One-time utility to generate testdata.xlsx. Run main() from IDE if Excel file is missing.
 */
public final class TestDataGenerator {

    private TestDataGenerator() {
    }

    public static void main(String[] args) throws IOException {
        Path output = Paths.get("src", "main", "resources", "testdata.xlsx");
        Files.createDirectories(output.getParent());

        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            createLoginSheet(workbook);
            createRegisterSheet(workbook);
            createTransferSheet(workbook);
            createBillPaySheet(workbook);
            createLoanSheet(workbook);

            try (FileOutputStream fos = new FileOutputStream(output.toFile())) {
                workbook.write(fos);
            }
        }
        System.out.println("Created " + output.toAbsolutePath());
    }

    private static void createLoginSheet(XSSFWorkbook workbook) {
        Sheet sheet = workbook.createSheet("Login");
        String[][] data = {
                {"testCase", "username", "password", "expectedError"},
                {"TC02", "invalid_user", "demo", "could not be verified"},
                {"TC03", "john", "wrongpass", "could not be verified"},
                {"TC04", "", "demo", "Please enter a username"},
                {"TC05", "john", "", "please enter a username and password"}
        };
        writeRows(sheet, data);
    }

    private static void createRegisterSheet(XSSFWorkbook workbook) {
        Sheet sheet = workbook.createSheet("Register");
        String[][] data = {
                {"testCase", "firstName", "lastName", "address", "city", "state", "zipCode", "phone", "ssn", "username", "password", "confirmPassword"},
                {"TC08", "", "", "", "", "", "", "", "", "", "", ""},
                {"TC09", "Jane", "Smith", "789 Pine Rd", "Boston", "MA", "02109", "6175550000", "987-65-4321", "john", "password123", "password123"},
                {"TC10", "Jane", "Smith", "789 Pine Rd", "Boston", "MA", "02109", "6175550000", "987-65-4321", "newuser_test", "password123", "password456"}
        };
        writeRows(sheet, data);
    }

    private static void createTransferSheet(XSSFWorkbook workbook) {
        Sheet sheet = workbook.createSheet("TransferFunds");
        String[][] data = {
                {"testCase", "amount", "fromIndex", "toIndex", "shouldPass"},
                {"Same Account", "100", "0", "0", "false"},
                {"Zero Amount", "0", "0", "1", "false"},
                {"Negative Amount", "-50", "0", "1", "false"},
                {"Large Amount", "999999", "0", "1", "false"}
        };
        writeRows(sheet, data);
    }

    private static void createBillPaySheet(XSSFWorkbook workbook) {
        Sheet sheet = workbook.createSheet("BillPay");
        String[][] data = {
                {"testCase", "name", "address", "city", "state", "zipCode", "phone", "accountNumber", "verifyAccount", "amount", "shouldPass"},
                {"Mandatory Fields", "", "", "", "", "", "", "", "", "", "false"},
                {"Invalid Account", "Test Payee", "123 St", "Boston", "MA", "02108", "6175551111", "12345", "54321", "25.00", "false"},
                {"Invalid Zip", "Test Payee", "123 St", "Boston", "MA", "ABC", "6175551111", "12345", "12345", "25.00", "false"}
        };
        writeRows(sheet, data);
    }

    private static void createLoanSheet(XSSFWorkbook workbook) {
        Sheet sheet = workbook.createSheet("Loan");
        String[][] data = {
                {"testCase", "amount", "downPayment", "expectedResult"},
                {"Loan Denied", "1000", "5000", "denied"},
                {"Zero Amount", "0", "0", "denied"},
                {"Large Amount", "999999", "100", "denied"},
                {"Invalid Down Payment", "5000", "10000", "denied"}
        };
        writeRows(sheet, data);
    }

    private static void writeRows(Sheet sheet, String[][] data) {
        for (int i = 0; i < data.length; i++) {
            Row row = sheet.createRow(i);
            for (int j = 0; j < data[i].length; j++) {
                row.createCell(j).setCellValue(data[i][j]);
            }
        }
    }
}
