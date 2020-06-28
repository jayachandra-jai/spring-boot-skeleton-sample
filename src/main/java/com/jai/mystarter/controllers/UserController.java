package com.jai.mystarter.controllers;

import com.jai.mystarter.models.auth.ERole;
import com.jai.mystarter.models.auth.Role;
import com.jai.mystarter.models.auth.User;
import com.jai.mystarter.models.dto.user.AddUser;
import com.jai.mystarter.models.dto.user.EditUser;
import com.jai.mystarter.models.dto.utils.Constants;
import com.jai.mystarter.models.dto.utils.GenericResponse;
import com.jai.mystarter.models.dto.utils.GenericValidation;
import com.jai.mystarter.repository.RoleRepository;
import com.jai.mystarter.repository.UserRepository;
import com.jai.mystarter.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user")
public class UserController {


    @Autowired
    UserRepository userRepository;


    @Autowired
    private UserService userService;

    @RequestMapping(value = "/sign-up", method = RequestMethod.POST)
    public ResponseEntity<?> addUser(@RequestBody AddUser addUser, Principal principal){
        log.info("at sign-up: "+addUser);
        GenericResponse genericResponse=new GenericResponse();
        GenericValidation validation=userService.isValidUser(addUser);
        if(validation.isValid()){
           User user= userService.addUser(addUser,principal.getName());
           if(null!=user) {
               log.info("Added user",user);
               genericResponse.setStatus(Constants.SUCCESS);
               genericResponse.setMessage("User Created");
           }else {
               log.info("Adding user Failed",addUser);
                genericResponse.setStatus(Constants.FAILED);
                genericResponse.setMessage("User Creation Failed");
           }
        }else {
            log.info("Invalid user",addUser);
            genericResponse.setStatus(Constants.FAILED);
            genericResponse.setMessage(validation.getReason());
        }
        return new ResponseEntity<GenericResponse>(genericResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "/", method = RequestMethod.PUT)
    public ResponseEntity<?> updateUser(@RequestBody EditUser editUser, Principal principal){
        return new ResponseEntity<GenericResponse>(userService.validUpdateUser(editUser,principal.getName()), HttpStatus.OK);
    }

    @RequestMapping(value = "/user-details", method = RequestMethod.GET)
    public ResponseEntity<?> getUser(Principal principal){
        return new ResponseEntity<User>(userService.getUserByUsername(principal.getName()), HttpStatus.OK);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ResponseEntity<?> getAllUsers(Principal principal){
        return new ResponseEntity<List<User>>(userService.getAllUsers(), HttpStatus.OK);
    }

    @RequestMapping(value = "/add-first-admin", method = RequestMethod.GET)
    public ResponseEntity<?> addDummyAdmin(){
        log.info("Adding First Admin");
        return new ResponseEntity<GenericValidation>(userService.addDummyUser() , HttpStatus.OK);
    }


}