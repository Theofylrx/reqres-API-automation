package com.reqres.models;

import java.util.List;

/**
 * Response model for user API calls
 */
public class UserResponse {
    private int page;
    private int per_page;
    private int total;
    private int total_pages;
    private List<User> data;
    private Support support;
    
    public static class Support {
        private String url;
        private String text;
        
        public String getUrl() {
            return url;
        }
        
        public void setUrl(String url) {
            this.url = url;
        }
        
        public String getText() {
            return text;
        }
        
        public void setText(String text) {
            this.text = text;
        }
    }
    
    // Getters and Setters
    public int getPage() {
        return page;
    }
    
    public void setPage(int page) {
        this.page = page;
    }
    
    public int getPer_page() {
        return per_page;
    }
    
    public void setPer_page(int per_page) {
        this.per_page = per_page;
    }
    
    public int getTotal() {
        return total;
    }
    
    public void setTotal(int total) {
        this.total = total;
    }
    
    public int getTotal_pages() {
        return total_pages;
    }
    
    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }
    
    public List<User> getData() {
        return data;
    }
    
    public void setData(List<User> data) {
        this.data = data;
    }
    
    public Support getSupport() {
        return support;
    }
    
    public void setSupport(Support support) {
        this.support = support;
    }
}