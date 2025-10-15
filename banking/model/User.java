package com.banking.model;

import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String username;
    private String passwordHash;
    private String salt;
    private boolean isActive;
    
    public User(String username, String passwordHash, String salt) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.salt = salt;
        this.isActive = true;
    }
    
    // Getters and setters
    public String getUsername() { return username; }
    public String getPasswordHash() { return passwordHash; }
    public String getSalt() { return salt; }
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
    
    @Override
    public String toString() {
        return username + "," + passwordHash + "," + salt + "," + isActive;
    }
    
    public static User fromString(String data) {
        String[] parts = data.split(",");
        if (parts.length == 4) {
            User user = new User(parts[0], parts[1], parts[2]);
            user.setActive(Boolean.parseBoolean(parts[3]));
            return user;
        }
        return null;
    }
}