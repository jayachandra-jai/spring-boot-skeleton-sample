package com.jai.mystarter.models.dto.user;

import lombok.Data;

@Data
public class EditUser {
    private String username;
    private String full_name;
    private String email;
    private String phone;
    private  String role;
}
