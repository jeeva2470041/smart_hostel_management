package com.college.hostel.model;

import javax.persistence.*;

@MappedSuperclass
public abstract class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private String email;
    private String role; // "STUDENT" or "ADMIN"

    
    protected String lastLoginTime;   
    protected String profileStatus;   
    public User() {}

    public User(String username, String password, String email, String role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    // Abstract method (must be implemented in subclasses)
    public abstract String getUserType();

    // Getters and Setters
    public Long getId() { return id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    // ✅ Optional getters/setters for the new fields
    public String getLastLoginTime() { return lastLoginTime; }
    public void setLastLoginTime(String lastLoginTime) { this.lastLoginTime = lastLoginTime; }

    public String getProfileStatus() { return profileStatus; }
    public void setProfileStatus(String profileStatus) { this.profileStatus = profileStatus; }
}
