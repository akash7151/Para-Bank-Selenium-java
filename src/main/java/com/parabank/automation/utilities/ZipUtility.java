package com.parabank.automation.utilities;

import com.parabank.automation.exceptions.FrameworkException;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public final class ZipUtility {

    private ZipUtility() {
    }

    public static Path zipDirectory(Path sourceDir, Path zipFile) {
        if (!Files.isDirectory(sourceDir)) {
            throw new FrameworkException("Directory not found for zipping: " + sourceDir);
        }
        try {
            Files.createDirectories(zipFile.getParent());
            if (Files.exists(zipFile)) {
                Files.delete(zipFile);
            }
            try (OutputStream outputStream = Files.newOutputStream(zipFile);
                 ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream)) {
                Files.walkFileTree(sourceDir, new SimpleFileVisitor<>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        String entryName = sourceDir.relativize(file).toString().replace('\\', '/');
                        zipOutputStream.putNextEntry(new ZipEntry(entryName));
                        Files.copy(file, zipOutputStream);
                        zipOutputStream.closeEntry();
                        return FileVisitResult.CONTINUE;
                    }
                });
            }
            return zipFile;
        } catch (IOException e) {
            throw new FrameworkException("Failed to zip directory: " + sourceDir, e);
        }
    }
}
