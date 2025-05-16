package com.reqres.api;

import java.util.List;

import org.json.JSONObject;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.reqres.util.api.RestUtil;
import com.reqres.util.data.TestDataFactory;
import com.reqres.util.data.TestDataLoader;
import com.reqres.util.data.TestDataManager;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.qameta.allure.restassured.AllureRestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

@Feature("User API Tests")
public class UserApiTests {
    private int userIdToTest;
    private TestDataManager dataManager;
    private JSONObject testData;
    private static final String API_KEY = "reqres-free-v1";
    private static final String HEADER = "x-api-key";
    
    @BeforeClass
    public void setup() {
        // Set up RestAssured
        RestUtil.setupRestAssured();
        
        // Initialize test data manager
        dataManager = new TestDataManager();
        
        // Load test data from JSON file
        testData = TestDataLoader.getUserData();
    }
    
    @AfterClass
    public void cleanup() {
        // Clear shared test data after tests complete
        dataManager.clearData();
        TestDataLoader.clearCache();
    }
    
    /**
     * Test Case 1: GET /api/users
     * Validate status code is 200
     * Validate that the response contains a list of users
     * Count the amount of users returned on the current page
     */
    @Test(priority = 1)
    @Description("Verify that the GET /users endpoint returns a list of users")
    @Severity(SeverityLevel.CRITICAL)
    @Story("List Users")
    public void testGetUsers() {
        System.out.println("Running Test 1: GET /api/users");
        
        // Get pagination settings from test data
        JSONObject paginationData = TestDataLoader.getPaginationData();
        int defaultPerPage = paginationData.getInt("default_per_page");
        
        Response response = given()
                .filter(new AllureRestAssured())
                .spec(RestUtil.getRequestSpec())
                .header(HEADER, API_KEY)
                .when()
                .get("/users")
                .then()
                .statusCode(200)
                .extract().response();
        
        JsonPath jsonPath = response.jsonPath();
        List<Object> userList = jsonPath.getList("data");
        
        assertNotNull(userList, "User list should not be null");
        assertFalse(userList.isEmpty(), "User list should not be empty");
        
        int usersCount = userList.size();
        int perPage = jsonPath.getInt("per_page");
        assertEquals(usersCount, perPage, "Number of users should match per_page value");
        assertEquals(perPage, defaultPerPage, "Per page value should match expected default");
        
        System.out.println("Users on current page: " + usersCount);
    }
    
    /**
     * Test Case 2: GET /api/users with query params
     * Add query params that will return full list of users
     * Extract the id of the user where email = 'charles.morris@reqres.in'
     */
    @Test(priority = 2)
    @Description("Verify that a specific user can be found by email")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Find User by Email")
    public void testGetAllUsersAndExtractId() {
        System.out.println("Running Test 2: GET /api/users with query params");
        
        // Get test data
        JSONObject defaultUser = testData.getJSONObject("default");
        String emailToFind = defaultUser.getString("email");
        
        // Get pagination settings from test data
        JSONObject paginationData = TestDataLoader.getPaginationData();
        int defaultPage = paginationData.getInt("default_page");
        int defaultPerPage = paginationData.getInt("default_per_page");
        
        // Request all users (page 1)
        Response response = given()
                .filter(new AllureRestAssured())
                .spec(RestUtil.getRequestSpec())
                .header(HEADER, API_KEY)
                .queryParam("page", defaultPage)
                .queryParam("per_page", defaultPerPage)
                .when()
                .get("/users")
                .then()
                .statusCode(200)
                .extract().response();
        
        JsonPath jsonPath = response.jsonPath();
        
        // Find user with specified email
        List<Integer> ids = jsonPath.getList("data.findAll { user -> user.email == '" + emailToFind + "' }.id");
        
        assertFalse(ids.isEmpty(), "User with email " + emailToFind + " should exist");
        
        userIdToTest = ids.get(0);
        
        // Store user ID for use in other tests
        dataManager.storeData(TestDataManager.KEY_USER_ID, userIdToTest);
        dataManager.storeData(TestDataManager.KEY_USER_EMAIL, emailToFind);
        
        System.out.println("Found user ID: " + userIdToTest + " for email: " + emailToFind);
    }
    
    /**
     * Test Case 3: GET /api/users/{id}
     * Use the id retrieved in step 2 to GET single user
     * Validate that the response contains correct user details
     */
    @Test(priority = 3, dependsOnMethods = "testGetAllUsersAndExtractId")
    @Description("Verify that a single user can be retrieved by ID")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Get Single User")
    public void testGetSingleUser() {
        System.out.println("Running Test 3: GET /api/users/{id}");
        
        // Get test data
        JSONObject defaultUser = testData.getJSONObject("default");
        String expectedEmail = defaultUser.getString("email");
        String expectedFirstName = defaultUser.getString("first_name");
        String expectedLastName = defaultUser.getString("last_name");
        
        // Retrieve user ID from test data manager
        int userId = dataManager.retrieveData(TestDataManager.KEY_USER_ID);
        
        Response response = given()
                .filter(new AllureRestAssured())
                .spec(RestUtil.getRequestSpec())
                .header(HEADER, API_KEY)
                .pathParam("id", userId)
                .when()
                .get("/users/{id}")
                .then()
                .statusCode(200)
                .extract().response();
        
        JsonPath jsonPath = response.jsonPath();
        
        String email = jsonPath.getString("data.email");
        String firstName = jsonPath.getString("data.first_name");
        String lastName = jsonPath.getString("data.last_name");
        
        assertEquals(email, expectedEmail, "Email should match");
        assertEquals(firstName, expectedFirstName, "First name should match");
        assertEquals(lastName, expectedLastName, "Last name should match");
        
        // Store user details for use in other tests
        dataManager.storeData(TestDataManager.KEY_USER_FIRST_NAME, firstName);
        dataManager.storeData(TestDataManager.KEY_USER_LAST_NAME, lastName);
    }
    
    /**
     * Test Case 4: POST /api/users
     * Send a request with name and job
     * Validate status 201 and that response contains the sent data
     */
    @Test(priority = 4)
    @Description("Verify that a new user can be created")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Create User")
    public void testCreateUser() {
        System.out.println("Running Test 4: POST /api/users");
        
        // Get test data
        JSONObject createUserData = testData.getJSONObject("create");
        String name = createUserData.getString("name");
        String job = createUserData.getString("job");
        
        // Create user request data using factory
        JSONObject userJson = TestDataFactory.createUserData(name, job);
        
        // Send POST request
        Response response = given()
                .filter(new AllureRestAssured())
                .spec(RestUtil.getRequestSpec())
                .header(HEADER, API_KEY)
                .body(userJson.toString())
                .when()
                .post("/users")
                .then()
                .statusCode(201)
                .extract().response();
        
        JsonPath jsonPath = response.jsonPath();
        String createdId = jsonPath.getString("id");

        assertNotNull(createdId, "Created user should have an ID");
        assertEquals(jsonPath.getString("name"), name, "Name should match");
        assertEquals(jsonPath.getString("job"), job, "Job should match");
        assertNotNull(jsonPath.getString("createdAt"), "Created date should be present");
        
        // Store created user ID
        dataManager.storeData(TestDataManager.KEY_CREATED_USER_ID, createdId);
    }
    
    /**
     * Test Case 5: PUT /api/users/{id}
     * Update a user's job
     * Validate the status is 200 and that updated data is returned
     */
    @Test(priority = 5, dependsOnMethods = "testGetAllUsersAndExtractId")
    @Description("Verify that a user can be updated")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Update User")
    public void testUpdateUser() {
        System.out.println("Running Test 5: PUT /api/users/{id}");
        
        // Get test data
        JSONObject updateUserData = testData.getJSONObject("update");
        String updatedName = updateUserData.getString("name");
        String updatedJob = updateUserData.getString("job");
        
        // Retrieve user ID from test data manager
        int userId = dataManager.retrieveData(TestDataManager.KEY_USER_ID);
        
        // Update user request data using factory
        JSONObject updateJson = TestDataFactory.updateUserData(updatedName, updatedJob);
        
        // Send PUT request
        Response response = given()
                .filter(new AllureRestAssured())    
                .spec(RestUtil.getRequestSpec())
                .header(HEADER, API_KEY)
                .pathParam("id", userId)
                .body(updateJson.toString())
                .when()
                .put("/users/{id}")
                .then()
                .statusCode(200)
                .extract().response();
        
        JsonPath jsonPath = response.jsonPath();
        
        assertEquals(jsonPath.getString("name"), updatedName, "Updated name should match");
        assertEquals(jsonPath.getString("job"), updatedJob, "Updated job should match");
        assertNotNull(jsonPath.getString("updatedAt"), "Updated date should be present");
    }
    
    /**
     * Test Case 6: DELETE /api/users/{id}
     * Validate status code 204 (no content)
     */
    @Test(priority = 6, dependsOnMethods = "testGetAllUsersAndExtractId")
    @Description("Verify that a user can be deleted")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Delete User")
    public void testDeleteUser() {
        System.out.println("Running Test 6: DELETE /api/users/{id}");
        
        // Retrieve user ID from test data manager
        int userId = dataManager.retrieveData(TestDataManager.KEY_USER_ID);
        
        given()
                .filter(new AllureRestAssured())
                .spec(RestUtil.getRequestSpec())
                .header(HEADER, API_KEY)
                .pathParam("id", userId)
                .when()
                .delete("/users/{id}")
                .then()
                .statusCode(204);
        
        System.out.println("User with ID " + userId + " successfully deleted");
    }
}