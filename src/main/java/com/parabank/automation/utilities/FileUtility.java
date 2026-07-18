package com.parabank.automation.utilities;

import com.parabank.automation.exceptions.FrameworkException;
import com.parabank.automation.logger.Log;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class FileUtility {

    private FileUtility() {
    }

    public static void createDirectoryIfNotExists(String directoryPath) {
        try {
            Files.createDirectories(Path.of(directoryPath));
        } catch (IOException e) {
            throw new FrameworkException("Failed to create directory: " + directoryPath, e);
        }
    }

    public static boolean fileExists(String filePath) {
        return Files.exists(Path.of(filePath));
    }

    public static void deleteFileIfExists(String filePath) {
        try {
            Files.deleteIfExists(Path.of(filePath));
        } catch (IOException e) {
            Log.warn(FileUtility.class, "Unable to delete file: " + filePath);
        }
    }
}
