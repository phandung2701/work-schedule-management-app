package com.example.btl_nhom4.model.user;

import java.util.List;

public class User {
    private String username;
    private String email;
    private String uid;
    private List<Workspace> workspace;
    private String dateOfEmployment;

    public User(String username, String email,String uid) {
        this.username = username;
        this.email = email;
        this.uid = uid;
    }

    public User(String username, String email, String uid, String dateOfEmployment) {
        this.username = username;
        this.email = email;
        this.uid = uid;
        this.dateOfEmployment = dateOfEmployment;
    }

    public User() {
    }

    public String getDateOfEmployment() {
        return dateOfEmployment;
    }

    public void setDateOfEmployment(String dateOfEmployment) {
        this.dateOfEmployment = dateOfEmployment;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User(String username, String email, List<Workspace> workspace) {
        this.username = username;
        this.email = email;
        this.workspace = workspace;
    }

    public List<Workspace> getWorkspace() {
        return workspace;
    }

    public void setWorkspace(List<Workspace> workspace) {
        this.workspace = workspace;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public User(String username, String email, String uid, List<Workspace> workspace) {
        this.username = username;
        this.email = email;
        this.uid = uid;
        this.workspace = workspace;
    }
}
