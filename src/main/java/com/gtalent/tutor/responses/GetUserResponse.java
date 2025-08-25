package com.gtalent.tutor.responses;

import com.gtalent.tutor.models.User;

import java.util.List;

public class GetUserResponse {
    private  int id;
    private String username;
    private String role;

    public GetUserResponse(){

    }

    public GetUserResponse(int id, String username) {
        this.id = id;
        this.username = username;
    }

    public GetUserResponse(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.role = user.getRole();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
