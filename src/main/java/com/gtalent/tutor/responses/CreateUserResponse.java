package com.gtalent.tutor.responses;

public class CreateUserResponse {
    private String username;

    public CreateUserResponse(){

    }

    public CreateUserResponse(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
