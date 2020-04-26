package com.jai.mystarter.models.dto;

import javax.validation.constraints.NotBlank;

/**
 * Created by Mohit on 16/05/18.
 */
public class Login {

    @NotBlank
    String username;

    @NotBlank
    String password;

    public String getUsername() {
        return username;
    }

    public Login setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public Login setPassword(String password) {
        this.password = password;
        return this;
    }

    @Override
    public String toString() {
        return "Login{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
