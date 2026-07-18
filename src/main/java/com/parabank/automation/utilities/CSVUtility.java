package com.parabank.automation.utilities;

import com.parabank.automation.exceptions.FrameworkException;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class CSVUtility {

    private CSVUtility() {
    }

    public static List<Map<String, String>> readCsv(Path filePath) {
        List<Map<String, String>> records = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String headerLine = reader.readLine();
            if (headerLine == null) {
                return records;
            }
            List<String> headers = parseLine(headerLine);
            String line;
            while ((line = reader.readLine()) != null) {
                List<String> values = parseLine(line);
                Map<String, String> row = new LinkedHashMap<>();
                for (int i = 0; i < headers.size() && i < values.size(); i++) {
                    row.put(headers.get(i), values.get(i));
                }
                records.add(row);
            }
        } catch (IOException exception) {
            throw new FrameworkException("Failed to read CSV file: " + filePath, exception);
        }
        return records;
    }

    public static void writeCsv(Path filePath, List<String> headers, List<List<String>> rows) {
        try {
            Files.createDirectories(filePath.getParent());
            List<String> lines = new ArrayList<>();
            lines.add(String.join(",", headers));
            for (List<String> row : rows) {
                lines.add(String.join(",", row));
            }
            Files.write(filePath, lines);
        } catch (IOException exception) {
            throw new FrameworkException("Failed to write CSV file: " + filePath, exception);
        }
    }

    private static List<String> parseLine(String line) {
        return Arrays.stream(line.split(",", -1))
                .map(String::trim)
                .toList();
    }
}
