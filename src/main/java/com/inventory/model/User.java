package com.inventory.model;

public class User {
    int id;
    String username;
    String password;
    String role;
    String email;
    boolean isVerified;

    public String getEmail() {
        return this.email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public User(int id, String username, String password, String role,String email, boolean isVerified) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.email = email;
        this.isVerified = isVerified;
    }
    public User( String username, String password, String role, String email, boolean isVerified) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.email = email;
        this.isVerified = isVerified;
    }
    public User(){

    }
    public int getId() {
        return id;
    }

    public String getRole() {
        return role;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
    private boolean verified;

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }
}




