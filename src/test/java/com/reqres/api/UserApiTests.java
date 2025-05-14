package com.reqres.api;

import java.util.List;

import org.json.JSONObject;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.reqres.util.RestUtil;

import static io.restassured.RestAssured.given;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class UserApiTests {
    private Integer userIdToTest;
    private final String EMAIL_TO_FIND = "charles.morris@reqres.in";
    
    @BeforeClass
    public void setup() {
        RestUtil.setupRestAssured();
    }
    
    /**
     * Test Case 1: GET /api/users
     * Validate status code is 200
     * Validate that the response contains a list of users
     * Count the amount of users returned on the current page
     */
    @Test(priority = 1)
    public void testGetUsers() {
        System.out.println("Running Test 1: GET /api/users");
        
        Response response = given()
                .spec(RestUtil.getRequestSpec())
                .when()
                .get("/users")
                .then()
                .statusCode(200)
                .extract().response();
        
        JsonPath jsonPath = response.jsonPath();
        List<Object> userList = jsonPath.getList("data");
        
        // Validations
        assertNotNull(userList, "User list should not be null");
        assertFalse(userList.isEmpty(), "User list should not be empty");
        
        // Count users on current page
        int usersCount = userList.size();
        int perPage = jsonPath.getInt("per_page");
        assertEquals(usersCount, perPage, "Number of users should match per_page value");
        
        System.out.println("Users on current page: " + usersCount);
    }
    
    /**
     * Test Case 2: GET /api/users with query params
     * Add query params that will return full list of users
     * Extract the id of the user where email = 'charles.morris@reqres.in'
     */
    @Test(priority = 2)
    public void testGetAllUsersAndExtractId() {
        System.out.println("Running Test 2: GET /api/users with query params");
        
        // Request all users (page 1)
        Response response = given()
                .spec(RestUtil.getRequestSpec())
                .queryParam("page", 1)
                .queryParam("per_page", 12)
                .when()
                .get("/users")
                .then()
                .statusCode(200)
                .extract().response();
        
        JsonPath jsonPath = response.jsonPath();
        
        // Find user with specified email
        List<Integer> ids = jsonPath.getList("data.findAll { user -> user.email == '" + EMAIL_TO_FIND + "' }.id");
        
        assertFalse(ids.isEmpty(), "User with email " + EMAIL_TO_FIND + " should exist");
        
        userIdToTest = ids.get(0);
        System.out.println("Found user ID: " + userIdToTest + " for email: " + EMAIL_TO_FIND);
    }
    
    /**
     * Test Case 3: GET /api/users/{id}
     * Use the id retrieved in step 2 to GET single user
     * Validate that the response contains correct user details
     */
    @Test(priority = 3, dependsOnMethods = "testGetAllUsersAndExtractId")
    public void testGetSingleUser() {
        System.out.println("Running Test 3: GET /api/users/{id}");
        
        Response response = given()
                .spec(RestUtil.getRequestSpec())
                .pathParam("id", userIdToTest)
                .when()
                .get("/users/{id}")
                .then()
                .statusCode(200)
                .extract().response();
        
        JsonPath jsonPath = response.jsonPath();
        
        // Extract user details
        String email = jsonPath.getString("data.email");
        String firstName = jsonPath.getString("data.first_name");
        String lastName = jsonPath.getString("data.last_name");
        
        // Validations
        assertEquals(email, EMAIL_TO_FIND, "Email should match");
        assertEquals(firstName, "Charles", "First name should match");
        assertEquals(lastName, "Morris", "Last name should match");
    }
    
    /**
     * Test Case 4: POST /api/users
     * Send a request with name and job
     * Validate status 201 and that response contains the sent data
     */
    @Test(priority = 4)
    public void testCreateUser() {
        System.out.println("Running Test 4: POST /api/users");
        
        // Create user request data
        JSONObject userJson = new JSONObject();
        userJson.put("name", "John Doe");
        userJson.put("job", "Software Tester");
        
        // Send POST request
        Response response = given()
                .spec(RestUtil.getRequestSpec())
                .body(userJson.toString())
                .when()
                .post("/users")
                .then()
                .statusCode(201)
                .extract().response();
        
        JsonPath jsonPath = response.jsonPath();
        
        // Validations
        assertNotNull(jsonPath.getString("id"), "Created user should have an ID");
        assertEquals(jsonPath.getString("name"), "John Doe", "Name should match");
        assertEquals(jsonPath.getString("job"), "Software Tester", "Job should match");
        assertNotNull(jsonPath.getString("createdAt"), "Created date should be present");
    }
    
    /**
     * Test Case 5: PUT /api/users/{id}
     * Update a user's job
     * Validate the status is 200 and that updated data is returned
     */
    @Test(priority = 5, dependsOnMethods = "testGetAllUsersAndExtractId")
    public void testUpdateUser() {
        System.out.println("Running Test 5: PUT /api/users/{id}");
        
        // Update user request data
        JSONObject updateJson = new JSONObject();
        updateJson.put("name", "John Updated");
        updateJson.put("job", "Senior Software Tester");
        
        // Send PUT request
        Response response = given()
                .spec(RestUtil.getRequestSpec())
                .pathParam("id", userIdToTest)
                .body(updateJson.toString())
                .when()
                .put("/users/{id}")
                .then()
                .statusCode(200)
                .extract().response();
        
        JsonPath jsonPath = response.jsonPath();
        
        // Validations
        assertEquals(jsonPath.getString("name"), "John Updated", "Updated name should match");
        assertEquals(jsonPath.getString("job"), "Senior Software Tester", "Updated job should match");
        assertNotNull(jsonPath.getString("updatedAt"), "Updated date should be present");
    }
    
    /**
     * Test Case 6: DELETE /api/users/{id}
     * Validate status code 204 (no content)
     */
    @Test(priority = 6, dependsOnMethods = "testGetAllUsersAndExtractId")
    public void testDeleteUser() {
        System.out.println("Running Test 6: DELETE /api/users/{id}");
        
        given()
                .spec(RestUtil.getRequestSpec())
                .pathParam("id", userIdToTest)
                .when()
                .delete("/users/{id}")
                .then()
                .statusCode(204);
        
        System.out.println("User with ID " + userIdToTest + " successfully deleted");
    }
}