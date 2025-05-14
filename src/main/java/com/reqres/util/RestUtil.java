package com.reqres.util;

import com.reqres.config.TestConfig;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

/**
 * Utility class for REST API interactions
 */
public class RestUtil {
    /**
     * Creates a base request specification with common settings
     * @return RequestSpecification with pre-configured settings
     */
    public static RequestSpecification getRequestSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(TestConfig.BASE_URL)
                .setContentType(ContentType.JSON)
                .addHeader("x-api-key", "reqres-free-v1")
                .build();
    }
    
    /**
     * Setup configuration for Rest Assured
     */
    public static void setupRestAssured() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }
}