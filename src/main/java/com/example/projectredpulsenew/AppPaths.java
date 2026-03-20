package com.example.projectredpulsenew;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public final class AppPaths {

    private static final Path PROJECT_DIR = Paths.get("").toAbsolutePath().normalize();
    private static final Path RESOURCE_DIR = Paths.get(
            "src", "main", "resources", "com", "example", "projectredpulsenew"
    ).toAbsolutePath().normalize();
    private static final Path DATA_DIR = PROJECT_DIR;

    private static final Path FILES_DIR = Paths.get("files").toAbsolutePath().normalize();
    private static final String LEGACY_PROJECT_PREFIX = "D:\\project-redpulse-new\\";
    private static final String LEGACY_FILES_PREFIX = LEGACY_PROJECT_PREFIX + "files\\";

    private AppPaths() {
    }

    public static Path userDetailsJson() {
        return ensureJsonFile("UserDetails.json");
    }

    public static Path postDetailsJson() {
        return ensureJsonFile("PostDetails.json");
    }

    public static Path notificationsJson() {
        return ensureJsonFile("Noti.json");
    }

    public static Path defaultImage() {
        return FILES_DIR.resolve("default.jpg");
    }

    public static File resolveDataFile(String storedPath) {
        if (storedPath == null || storedPath.isBlank()) {
            return null;
        }

        String normalized = storedPath.trim().replace("/", "\\");
        if (normalized.startsWith(LEGACY_FILES_PREFIX)) {
            String fileName = normalized.substring(LEGACY_FILES_PREFIX.length());
            return FILES_DIR.resolve(fileName).toFile();
        }

        if (normalized.startsWith("files\\")) {
            return FILES_DIR.resolve(normalized.substring("files\\".length())).toFile();
        }

        return Paths.get(storedPath).toAbsolutePath().normalize().toFile();
    }

    private static Path ensureJsonFile(String fileName) {
        Path dataFile = DATA_DIR.resolve(fileName);
        Path bundledFile = RESOURCE_DIR.resolve(fileName);

        try {
            if (Files.notExists(dataFile)) {
                copyOrInitialize(dataFile, bundledFile);
            } else if (shouldHydrateFromBundled(dataFile, bundledFile)) {
                Files.copy(bundledFile, dataFile, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not prepare data file: " + dataFile, e);
        }

        return dataFile;
    }

    private static void copyOrInitialize(Path dataFile, Path bundledFile) throws IOException {
        if (Files.exists(bundledFile)) {
            Files.copy(bundledFile, dataFile, StandardCopyOption.REPLACE_EXISTING);
            return;
        }

        Files.writeString(dataFile, "[]");
    }

    private static boolean shouldHydrateFromBundled(Path dataFile, Path bundledFile) throws IOException {
        if (Files.notExists(bundledFile)) {
            return false;
        }

        String currentContent = Files.readString(dataFile).trim();
        String bundledContent = Files.readString(bundledFile).trim();

        boolean currentIsEmptyArray = currentContent.isEmpty() || "[]".equals(currentContent);
        boolean bundledHasData = !bundledContent.isEmpty() && !"[]".equals(bundledContent);

        return currentIsEmptyArray && bundledHasData;
    }
}
