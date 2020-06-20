package com.jai.mystarter.controllers;

import com.jai.mystarter.models.auth.ERole;
import com.jai.mystarter.models.auth.Role;
import com.jai.mystarter.models.auth.User;
import com.jai.mystarter.models.dto.user.AddUser;
import com.jai.mystarter.models.dto.utils.GenericResponse;
import com.jai.mystarter.repository.RoleRepository;
import com.jai.mystarter.repository.UserRepository;
import com.jai.mystarter.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class UserController {


    @Autowired
    UserRepository userRepository;


    @Autowired
    private UserService userService;

    @RequestMapping(value = "/sign-up", method = RequestMethod.POST)
    public ResponseEntity<?> addUser(@RequestBody AddUser addUser, Principal principal){
        log.info("at sign-up: "+addUser);
        GenericResponse genericResponse=new GenericResponse();
        if(userService.isValidUser(addUser)){
           User user= userService.addUser(addUser,principal.getName());
           if(null!=user) {
               log.info("Added user",user);
               genericResponse.setStatus(0);
               genericResponse.setMessage("User Created");
           }else {
               log.info("Adding user Failed",addUser);
                genericResponse.setStatus(2);
                genericResponse.setMessage("User Creation Failed");
           }
        }else {
            log.info("Invalid user",addUser);
            genericResponse.setStatus(2);
            genericResponse.setMessage("Invalid Parameters");
        }
        return new ResponseEntity<GenericResponse>(genericResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "/add-first-admin", method = RequestMethod.GET)
    public ResponseEntity<?> dummyAdmin(){
        log.info("Adding First Admin");
        return new ResponseEntity<String>("Test API: "+userService.addDummyUser() , HttpStatus.OK);
    }


}