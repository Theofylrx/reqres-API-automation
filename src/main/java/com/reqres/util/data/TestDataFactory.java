package com.reqres.util.data;

import org.json.JSONObject;

/**
 * Factory class to generate test data for API requests
 */
public class TestDataFactory {

    // Private constructor to prevent instantiation
    private TestDataFactory() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    /**
     * Create a JSON object for creating a new user
     * @param name User name
     * @param job User job
     * @return JSONObject with user data
     */
    public static JSONObject createUserData(String name, String job) {
        JSONObject userJson = new JSONObject();
        userJson.put("name", name);
        userJson.put("job", job);
        return userJson;
    }
    
    /**
     * Create a JSON object for updating a user
     * @param name Updated user name
     * @param job Updated user job
     * @return JSONObject with updated user data
     */
    public static JSONObject updateUserData(String name, String job) {
        return createUserData(name, job);
    }
    
    /**
     * Predefined test data: Standard user creation
     * @return JSONObject with standard test user data
     */
    public static JSONObject getStandardUserData() {
        return createUserData("John Doe", "Software Tester");
    }
    
    /**
     * Predefined test data: Updated user
     * @return JSONObject with updated user data
     */
    public static JSONObject getUpdatedUserData() {
        return updateUserData("John Updated", "Senior Software Tester");
    }
}