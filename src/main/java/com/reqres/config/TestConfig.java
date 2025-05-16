package com.reqres.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Configuration class for API endpoints.
 * Loads configuration values from environment.properties file.
 */
public final class TestConfig {
    
    private static final Properties properties = new Properties();
    
    /**
     * Base URL for the ReqRes API in the production environment.
     */
    public static final String PROD_BASE_URL;
    
    /**
     * Base URL for the ReqRes API in the test environment.
     */
    public static final String TEST_BASE_URL;
    
    /**
     * Current active base URL, determined by environment configuration.
     */
    public static final String BASE_URL;
    
    /**
     * Timeout value for API requests in milliseconds.
     */
    public static final int REQUEST_TIMEOUT;
    
    // Static initializer to load properties when class is loaded
    static {
        loadProperties();
        
        // Initialize constants from properties
        PROD_BASE_URL = properties.getProperty("prod.base.url", "https://reqres.in/api");
        TEST_BASE_URL = properties.getProperty("test.base.url", "https://test.reqres.in/api");
        REQUEST_TIMEOUT = Integer.parseInt(properties.getProperty("request.timeout", "5000"));
        
        // Set base URL based on environment
        BASE_URL = properties.getProperty("base.url", determineEnvironment().equals("prod") ? PROD_BASE_URL : TEST_BASE_URL);
    }
    
    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private TestConfig() {
        throw new AssertionError("Utility class - should not be instantiated");
    }
    
    /**
     * Loads properties from the appropriate environment properties file.
     * Tries to load environment-specific file first, falls back to default.
     */
    private static void loadProperties() {
        // Determine environment
        String env = determineEnvironment();
        
        // Try to load environment-specific properties first
        String envSpecificFile = "environment-" + env + ".properties";
        boolean loaded = loadPropertiesFile(envSpecificFile);
        
        // If not found or failed, load default properties
        if (!loaded) {
            loadPropertiesFile("environment.properties");
        }
    }
    
    /**
     * Helper method to load a specific properties file.
     * 
     * @param filename Name of the properties file to load
     * @return true if file was loaded successfully, false otherwise
     */
    private static boolean loadPropertiesFile(String filename) {
        try (InputStream input = TestConfig.class.getClassLoader().getResourceAsStream(filename)) {
            if (input != null) {
                properties.load(input);
                System.out.println("Loaded configuration from " + filename);
                return true;
            } else {
                System.err.println("Warning: " + filename + " not found.");
                return false;
            }
        } catch (IOException e) {
            System.err.println("Error loading " + filename + ": " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Determines which environment to use.
     * Checks for environment in this order:
     * 1. System property "api.environment"
     * 2. Environment variable "API_ENVIRONMENT"
     * 3. Default to "test" if neither is set
     *
     * @return The environment name (prod, test, etc.)
     */
    private static String determineEnvironment() {
        // First check system property (can be set via Gradle)
        String env = System.getProperty("env");
        //TODO: need to check why env is not being set via the command line
        
        // If not found, check environment variable
        if (env == null) {
            env = System.getenv("env");
            //TODO: need to check why env is not being set via the command line
        }
        
        // Default to prod if not specified
        if (env == null) {
            env = "prod";
        }
        
        return env;
    }
    
    /**
     * Gets a custom property from the properties file.
     * Useful for accessing additional configuration values.
     *
     * @param key The property key
     * @param defaultValue Default value if property is not found
     * @return The property value or default if not found
     */
    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
}