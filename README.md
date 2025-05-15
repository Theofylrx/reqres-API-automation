# ReqRes - API - Automation

This project demonstrates automated API testing for the [ReqRes API](https://reqres.in/) using Rest Assured, TestNG, and Gradle.

## Project Overview

The project tests the following API endpoints:
1. **GET /api/users**
   - Validates status code is 200
   - Validates that the response contains a list of users
   - Counts the amount of users returned on the current page

2. **GET /api/users with Query Parameters**
   - Adds query params that will return the full list of users
   - Extracts the id of the user where email = 'charles.morris@reqres.in'

3. **GET /api/users/{id}**
   - Uses the id retrieved in step 2 to GET a single user
   - Validates that the response contains correct user details

4. **POST /api/users**
   - Sends a request with name and job
   - Validates status 201 and that response contains the sent data

5. **PUT /api/users/{id}**
   - Updates a user's job
   - Validates the status is 200 and that updated data is returned

6. **DELETE /api/users/{id}**
   - Validates status code 204 (no content)

## Prerequisites
- Java 8 or higher
- Gradle
- Rest-Assured
- TestNG
- Allure reports
- Github actions
- VS Code with extensions for Java development

## VS Code Extensions Required
1. **Extension Pack for Java** - Microsoft (vscjava.vscode-java-pack)
2. **Gradle for Java** - Microsoft (vscjava.vscode-gradle)

## Project Structure
```
├── .github                                                             # 
├── src
│   ├── main
│   │    ├── /java/reqres/api/automation                                #
│   │    │                      ├── config
│   │    │                      │      └── TestConfig.java              #
│   │    │                      └── util
│   │    │                             ├── api                          #
│   │    │                             │    └── RestUtil.java           #
│   │    │                             └── data
│   │    │                                  ├── TestDataFactory.java
│   │    │                                  ├── TestDataLoader.java
│   │    │                                  └── TestDataManager.java
│   │    └── /resources                                                 #
│   └── test
│        ├── /java/reqres/api/automation                                #
│        │                      └── UserApiTests.java                   #
│        └── /resources                                                 #
│                 ├── testdata
│                 │       └── testdata.json                             #
│                 ├── allure.properties                                 #
│                 └── testng.xml                                        #
│
├── build                                                               #
├── gradle                                                              #
│   ├── wrapper
│   │    ├── gradle-wrapper.jar                                         #
│   │    └── gradle-wrapper.properties                                  #
│   └── libs.versions.toml                                              #
│
├── .gitattributes                                                      #
├── .gitignore                                                          #
├── build.gradle.kts                                                    # 
├── gradlew                                                             # 
├── gradlew.bat                                                         # 
├── makefile                                                            # 
├── README.md                                                           # Project documentation
└── settings.gradle.kts                                                 # Gradle settings
```

## Setup Instructions

### Clone the Repository
```bash
git clone https://github.com/Theofylrx/reqres-API-automation
cd reqres-API-automation
```
### Clean the project
```bash
make clean
```
### Build the Project
```bash
make build
```
### Run Tests
```bash
make test
```
### Clean, Build & Run Tests
```bash
make .PHONY
```
### Create allure report
```bash
make report
```
### View allure report
```bash
make serve-report
```
## IDE Setup
### VS Code
1. Open VS Code
2. Install the "Extension Pack for Java" extension
3. Open the project folder
4. VS Code will automatically detect and set up the Gradle project

### Eclipse
1. Open Eclipse
2. Select File > Import > Gradle > Existing Gradle Project
3. Navigate to the project directory
4. Click "Finish"

### IntelliJ IDEA
1. Open IntelliJ IDEA
2. Select "Import Project"
3. Navigate to the project directory and select the build.gradle file
4. Click "Open"

## Test Data Management
This section outlines the test data management approach used in the ReqRes API automation project.

### Overview
The test data management solution consists of components that work together to provide a flexible, maintainable, and reusable test data approach.

### Components
1. **TestDataManager**
A singleton class that provides:

Storage and retrieval of data that needs to be shared between test methods
Default values for commonly used test data
Constants for data keys to prevent typos and ensure consistency

**Location:** 
```
src/main/java/com/reqres/api/data/TestDataManager.java
```
2. **TestDataFactory**
A factory class that creates test data objects for different API requests:

User creation data
User update data
Registration data
Login data
Predefined test data for common scenarios

**Location:** 
```
src/main/java/com/reqres/api/data/TestDataFactory.java
```
3. **External Test Data Files**
JSON files containing test data for different test scenarios:

User data
Authentication data
Pagination settings
Resource data

**Location:** 
```
src/test/resources/testdata/testdata.json
```
4. **TestDataLoader**
Utility class for loading and accessing test data from external files:

Loads JSON test data files
Provides access to specific sections of test data
Caches data for better performance

**Location:** 
```
src/main/java/com/reqres/api/data/TestDataLoader.java
```
### Usage Examples
#### Loading Test Data from Files
```bash
- Load the user section from test data
JSONObject userData = TestDataLoader.getUserData();

- Get a specific user's data
JSONObject defaultUser = userData.getJSONObject("default");
String emailToFind = defaultUser.getString("email");
```
#### Creating Test Data with Factory
```bash
- Create user data for a new user
JSONObject userJson = TestDataFactory.createUserData("John Doe", "Software Tester");

- Use predefined test data
JSONObject standardUser = TestDataFactory.getStandardUserData();
```
#### Sharing Data Between Test Methods
```bash
- Store data
TestDataManager dataManager = TestDataManager.getInstance();
dataManager.storeData(TestDataManager.KEY_USER_ID, userId);

- Retrieve data in another test method
int userId = dataManager.retrieveData(TestDataManager.KEY_USER_ID);
```
#### Best Practices
```
1. Use constants for data keys to prevent typos and ensure consistency.
2. Externalize test data in JSON files for better maintenance.
3. Use factory methods to create test data objects.
4. Share data between tests using the TestDataManager.
5. Clear shared data after tests complete to prevent test dependencies.
```