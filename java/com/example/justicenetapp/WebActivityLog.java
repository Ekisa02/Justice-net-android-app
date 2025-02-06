package com.example.justicenetapp;

public class WebActivityLog {
    private String url;
    private String timestamp;
    private String location;

    public WebActivityLog() {
        // Default constructor required for Firebase Firestore
    }

    public WebActivityLog(String url, String timestamp, String location) {
        this.url = url;
        this.timestamp = timestamp;
        this.location = location;
    }

    public String getUrl() {
        return url;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getLocation() {
        return location;
    }
}
