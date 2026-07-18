package com.parabank.automation.utilities;

import com.parabank.automation.exceptions.FrameworkException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public final class JsonUtility {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private JsonUtility() {
    }

    public static String toJson(Object object) {
        return GSON.toJson(object);
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        return GSON.fromJson(json, clazz);
    }

    public static Map<String, String> readMapFromFile(Path filePath) {
        try (Reader reader = Files.newBufferedReader(filePath)) {
            Type type = new TypeToken<Map<String, String>>() {}.getType();
            return GSON.fromJson(reader, type);
        } catch (IOException exception) {
            throw new FrameworkException("Failed to read JSON file: " + filePath, exception);
        }
    }

    public static void writeToFile(Object object, Path filePath) {
        try (Writer writer = Files.newBufferedWriter(filePath)) {
            GSON.toJson(object, writer);
        } catch (IOException exception) {
            throw new FrameworkException("Failed to write JSON file: " + filePath, exception);
        }
    }
}
