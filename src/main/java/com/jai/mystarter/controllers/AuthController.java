package com.jai.mystarter.controllers;

import com.jai.mystarter.models.auth.ERole;
import com.jai.mystarter.models.auth.Role;
import com.jai.mystarter.models.auth.User;
import com.jai.mystarter.repository.RoleRepository;
import com.jai.mystarter.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private Logger logger = LoggerFactory.getLogger(AuthController.class);
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder userPasswordEncoder;

    @RequestMapping(value = "/sign_up", method = RequestMethod.GET)
    public ResponseEntity<?> signUp(User user){
        logger.info("Sign Up");
        user.setUsername("jai.js");
        user.setPassword(userPasswordEncoder.encode("1234"));
        Set<Role> roleSet=new HashSet<>();
        Role role=new Role();
        role.setId(1);
        role.setName(ERole.ROLE_ADMIN);
        roleSet.add(role);
        user.setRoles(roleSet);
        userRepository.save(user);
        return new ResponseEntity<String>("Test API", HttpStatus.OK);
    }


}