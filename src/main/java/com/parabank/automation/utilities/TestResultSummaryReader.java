package com.parabank.automation.utilities;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;
import java.nio.file.Files;
import java.nio.file.Path;

public final class TestResultSummaryReader {

    private TestResultSummaryReader() {
    }

    public static String readSummary(Path testNgResultsFile) {
        if (!Files.isRegularFile(testNgResultsFile)) {
            return "No TestNG results file found at " + testNgResultsFile;
        }
        try {
            Document document = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder()
                    .parse(testNgResultsFile.toFile());
            Element root = document.getDocumentElement();
            String total = root.getAttribute("total");
            String passed = root.getAttribute("passed");
            String failed = root.getAttribute("failed");
            String skipped = root.getAttribute("skipped");
            return String.format(
                    "Total: %s | Passed: %s | Failed: %s | Skipped: %s",
                    total, passed, failed, skipped);
        } catch (Exception e) {
            return "Unable to parse TestNG summary: " + e.getMessage();
        }
    }
}
