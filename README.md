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
- VS Code with extensions for Java development

## VS Code Extensions Required
1. **Extension Pack for Java** - Microsoft (vscjava.vscode-java-pack)
2. **Gradle for Java** - Microsoft (vscjava.vscode-gradle)

## Project Structure
```
├── .github                                                 # 
├── src
│   ├── main
│   │    ├── /java/reqres/api/automation                    #
│   │    │                      ├── config
│   │    │                      │      └── TestConfig.java  #
│   │    │                      ├── models                  #
│   │    │                      └── util
│   │    │                             └── RestUtil.java    #
│   │    └── /resources                                     #
│   └── test
│        ├── /java/reqres/api/automation                    #
│        │                      └── UserApiTests.java       #
│        └── /resources                                     #
│                 └── allure.properties                     #
│
├── build                                                   #
├── gradle                                                  #
│   ├── wrapper
│   │    ├── gradle-wrapper.jar                             #
│   │    └── gradle-wrapper.properties                      #
│   └── libs.versions.toml                                  #
│
├── .gitattributes                                          #
├── .gitignore
├── build.gradle.kts                                        # 
├── gradlew                                                 # 
├── gradlew.bat                                             # 
├── makefile                                                # 
├── README.md                                               # Project documentation
└── settings.gradle.kts                                     # Gradle settings
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