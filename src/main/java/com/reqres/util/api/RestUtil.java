package com.reqres.util.api;

import com.reqres.config.TestConfig;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

/**
 * Utility class for REST API interactions
 */
public final class RestUtil {
    
    /**
     * Private constructor to prevent instantiation
     */
    private RestUtil() {
        throw new AssertionError("Utility class - should not be instantiated");
    }
    
    /**
     * Creates a base request specification with common settings
     * @return RequestSpecification with pre-configured settings
     */
    public static RequestSpecification getRequestSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(TestConfig.BASE_URL)
                .setContentType(ContentType.JSON)
                .setRelaxedHTTPSValidation() // For handling SSL certificates in testing
                .build();
    }
    
    /**
     * Setup configuration for Rest Assured
     */
    public static void setupRestAssured() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.requestSpecification = getRequestSpec();
    }
    
    /**
     * Setup more detailed logging for debugging purposes
     */
    public static void enableDetailedLogging() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }
}