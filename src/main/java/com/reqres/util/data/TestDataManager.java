package com.reqres.util.data;

import java.util.HashMap;
import java.util.Map;

/**
 * Class for managing test data in the ReqRes API tests
 */
public class TestDataManager {
    
    // Store for test data that needs to be shared between test methods
    private final Map<String, Object> sharedData;
    
    // Default test data values
    private static final String DEFAULT_EMAIL = "charles.morris@reqres.in";
    private static final int DEFAULT_PAGE = 1;
    private static final int DEFAULT_PER_PAGE = 6;
    
    /**
     * Constructor for TestDataManager
     */
    public TestDataManager() {
        sharedData = new HashMap<>();
    }
    
    /**
     * Store data to be shared between test methods
     * @param key The key to store the data under
     * @param value The value to store
     */
    public void storeData(String key, Object value) {
        sharedData.put(key, value);
    }
    
    /**
     * Retrieve shared data
     * @param key The key to retrieve data for
     * @return The stored data, or null if not found
     */
    @SuppressWarnings("unchecked")
    public <T> T retrieveData(String key) {
        return (T) sharedData.get(key);
    }
    
    /**
     * Clear all stored data
     */
    public void clearData() {
        sharedData.clear();
    }
    
    /**
     * Get the default email used for searching users
     * @return Default email address
     */
    public String getDefaultEmail() {
        return DEFAULT_EMAIL;
    }
    
    /**
     * Get default page number
     * @return Default page number
     */
    public int getDefaultPage() {
        return DEFAULT_PAGE;
    }
    
    /**
     * Get default per page count
     * @return Default per page count
     */
    public int getDefaultPerPage() {
        return DEFAULT_PER_PAGE;
    }
    
    // Constants for shared data keys
    public static final String KEY_USER_ID = "userId";
    public static final String KEY_USER_EMAIL = "userEmail";
    public static final String KEY_USER_FIRST_NAME = "userFirstName";
    public static final String KEY_USER_LAST_NAME = "userLastName";
    public static final String KEY_CREATED_USER_ID = "createdUserId";
}
