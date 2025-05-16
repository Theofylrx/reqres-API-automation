package com.reqres.util.data;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.json.JSONObject;

/**
 * Utility class for loading test data from external files
 */
public final class TestDataLoader {
    
    private static final String TEST_DATA_FILE = "/testdata/testdata.json";
    private static JSONObject testData;
    
    // Cache for parsed test data sections
    private static final Map<String, JSONObject> dataCache = new HashMap<>();
    
    /**
     * Private constructor to prevent instantiation of this utility class
     */
    private TestDataLoader() {
        // Utility class should not be instantiated
        throw new AssertionError("TestDataLoader is a utility class and should not be instantiated");
    }
    
    /**
     * Custom runtime exception for test data loading failures
     */
    public static class TestDataRuntimeException extends RuntimeException {
        public TestDataRuntimeException(String message) {
            super(message);
        }
        
        public TestDataRuntimeException(String message, Throwable cause) {
            super(message, cause);
        }
    }
    
    /**
     * Load the test data JSON file
     * @return JSONObject containing the test data
     */
    public static JSONObject loadTestData() {
        if (testData == null) {
            try (InputStream is = TestDataLoader.class.getResourceAsStream(TEST_DATA_FILE)) {
                if (is == null) {
                    throw new TestDataRuntimeException("Test data file not found: " + TEST_DATA_FILE);
                }
                try (Scanner scanner = new Scanner(is, StandardCharsets.UTF_8.name())) {
                    String jsonString = scanner.useDelimiter("\\A").next();
                    testData = new JSONObject(jsonString);
                }
            } catch (IOException e) {
                throw new TestDataRuntimeException("Failed to load test data file", e);
            }
        }
        return testData;
    }
    
    /**
     * Get a specific section from the test data file
     * @param section The section name (e.g., "users", "authentication")
     * @return JSONObject for the requested section
     */
    public static JSONObject getSection(String section) {
        if (!dataCache.containsKey(section)) {
            JSONObject data = loadTestData();
            if (data.has(section)) {
                dataCache.put(section, data.getJSONObject(section));
            } else {
                throw new TestDataRuntimeException("Section '" + section + "' not found in test data");
            }
        }
        return dataCache.get(section);
    }
    
    /**
     * Get user data from the test data file
     * @return JSONObject with user data
     */
    public static JSONObject getUserData() {
        return getSection("users");
    }
    
    /**
     * Get pagination settings from the test data file
     * @return JSONObject with pagination settings
     */
    public static JSONObject getPaginationData() {
        return getSection("pagination");
    }
    
    /**
     * Clear the test data cache
     */
    public static void clearCache() {
        dataCache.clear();
        testData = null;
    }
}