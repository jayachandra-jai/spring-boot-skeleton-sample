package com.jai.mystarter.models.dto.user;

import lombok.Data;

@Data
public class AddUser {
    private String username;
    private String password;
    private String full_name;
    private String email;
    private String phone;
    private  String role;
}
