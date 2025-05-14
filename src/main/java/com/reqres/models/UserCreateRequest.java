package com.reqres.models;

/**
 * User create request model
 */
public class UserCreateRequest {
    private String name;
    private String job;
    
    public UserCreateRequest(String name, String job) {
        this.name = name;
        this.job = job;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getJob() {
        return job;
    }
    
    public void setJob(String job) {
        this.job = job;
    }
}