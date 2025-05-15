package com.reqres.util.data;

import org.json.JSONObject;

/**
 * Factory class to generate test data for API requests
 */
public class TestDataFactory {
    
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
     * Create a JSON object for user registration
     * @param email User email
     * @param password User password
     * @return JSONObject with registration data
     */
    public static JSONObject registerUserData(String email, String password) {
        JSONObject registerJson = new JSONObject();
        registerJson.put("email", email);
        registerJson.put("password", password);
        return registerJson;
    }
    
    /**
     * Create a JSON object for user login
     * @param email User email
     * @param password User password
     * @return JSONObject with login data
     */
    public static JSONObject loginUserData(String email, String password) {
        return registerUserData(email, password);
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
    
    /**
     * Predefined test data: Standard registration
     * @return JSONObject with standard registration data
     */
    public static JSONObject getStandardRegistrationData() {
        return registerUserData("eve.holt@reqres.in", "pistol");
    }
}