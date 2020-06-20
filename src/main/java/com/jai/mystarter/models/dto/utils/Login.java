package com.jai.mystarter.models.dto.utils;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * Created by Mohit on 16/05/18.
 */
@Data
public class Login {

    @NotBlank
    String username;

    @NotBlank
    String password;


    @Override
    public String toString() {
        return "Login{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
