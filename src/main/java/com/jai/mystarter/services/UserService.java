package com.jai.mystarter.services;

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
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
@Slf4j
@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private PasswordEncoder userPasswordEncoder;

    @Autowired
    RoleRepository roleRepository;

    public User addUser(AddUser addUser,String createdBy){
        try {
            User user=new User();
            user.setUsername(addUser.getUsername());
            user.setPassword(userPasswordEncoder.encode(addUser.getPassword()));
            user.setPassword_updated_at(new Timestamp(System.currentTimeMillis()));
            user.setFull_name(addUser.getFull_name());
            user.setPhone(addUser.getPhone());
            user.setEmail(addUser.getEmail());
            user.setCreated_by(createdBy);
            user.setLast_updated_by(createdBy);
            Set<Role> roleSet=new HashSet<>();
            Role role=roleRepository.findByName(EnumUtils.getEnum(ERole.class,addUser.getRole()));
            roleSet.add(role);
            user.setRoles(roleSet);
            return userRepository.save(user);
        }catch (Exception e){
           log.error("Error  Adding User",e);
        }
        return null;
    }

    public User getUserByUsername(String username){
        try {
            User user=userRepository.findByUsername(username);
            return user;
        }catch (Exception e){
            log.error("Error in get user",e);
        }
        return null;
    }

    public GenericValidation isValidUser(AddUser addUser){
        GenericValidation validation=new GenericValidation();
        if(null!=addUser){
            if(!StringUtils.isEmpty(addUser.getUsername())
                    && !StringUtils.isEmpty(addUser.getPassword())
                    && !StringUtils.isEmpty(addUser.getFull_name())
                    && !StringUtils.isEmpty(addUser.getRole())){

                if(!EnumUtils.isValidEnum(ERole.class,addUser.getRole())){
                    validation.setValid(false);
                    validation.setReason("Invalid Role");
                }else if(null!=getUserByUsername(addUser.getUsername())){
                    validation.setValid(false);
                    validation.setReason("Username already Exists");
                }else {
                    validation.setValid(true);
                    validation.setReason("Valid User");
                }
            }else {
                validation.setValid(false);
                validation.setReason("One of these parameters are empty: username,password,full_name,role");
            }

        }else {
            validation.setValid(false);
            validation.setReason("Request is null");
        }
        return validation;
    }
    public GenericValidation addDummyUser(){
        GenericValidation validation=new GenericValidation();
        String username="superAdmin@js.live";
        try {

            User user=getUserByUsername(username);
            if(null==user){
                user=new User();
                user.setUsername(username);
                user.setPassword_updated_at(new Timestamp(System.currentTimeMillis()));
                user.setPassword(userPasswordEncoder.encode("12345678"));
                user.setFull_name("Super Admin");
                user.setUpdated_at(new Timestamp(System.currentTimeMillis()));
                user.setCreated_by(username);
                user.setLast_updated_by(username);
                Set<Role> roleSet=new HashSet<>();
                Role role=roleRepository.findByName(EnumUtils.getEnum(ERole.class,"SUPER_ADMIN"));
                roleSet.add(role);
                user.setRoles(roleSet);
                userRepository.save(user);
                validation.setValid(true);
                validation.setReason("Created SUPER ADMIN[username: "+username+" password: 12345678"+"]");
            }else {
                validation.setValid(true);
                validation.setReason("SUPER ADMIN Already Created[username: "+user.getUsername()+" password: 12345678"+"]");
            }

        }catch (Exception e){
            log.error("Error  Adding User",e);
            validation.setValid(false);
            validation.setReason(e.getMessage());
        }
        return validation;
    }

    public List<User> getAllUsers(){
        try {
            List<User> users= userRepository.findAll();
            if(null!=users)
                return users;
        }catch (Exception e){
           log.error("Error Get All users");
        }
        return new ArrayList<>();
    }

    public GenericResponse validUpdateUser(EditUser editUser, String updatedBy){
        GenericResponse validation=new GenericResponse();
        if(null==editUser){
            validation.setStatus(Constants.FAILED);
            validation.setMessage("Request is Empty");
            return validation;
        }

        if(StringUtils.isEmpty(editUser.getUsername())){
            validation.setStatus(Constants.FAILED);
            validation.setMessage("username is Empty");
            return validation;
        }

        if(StringUtils.isNotEmpty(editUser.getRole()) && !EnumUtils.isValidEnum(ERole.class,editUser.getRole())){
            validation.setStatus(Constants.FAILED);
            validation.setMessage("Invalid Role");
            return validation;
        }

        User user=getUserByUsername(editUser.getUsername());
        if(user!=null){
            if(StringUtils.isNotEmpty(editUser.getFull_name())) user.setFull_name(editUser.getFull_name());
            if(StringUtils.isNotEmpty(editUser.getEmail())) user.setEmail(editUser.getEmail());
            if(StringUtils.isNotEmpty(editUser.getPhone())) user.setPhone(editUser.getPhone());
            if(StringUtils.isNotEmpty(editUser.getRole())){
                Set<Role> roleSet=new HashSet<>();
                Role role=roleRepository.findByName(EnumUtils.getEnum(ERole.class,editUser.getRole()));
                roleSet.add(role);
                user.setRoles(roleSet);
            }
            try {
                user.setLast_updated_by(updatedBy);
                user.setUpdated_at(new Timestamp(System.currentTimeMillis()));
                userRepository.save(user);
                validation.setStatus(Constants.SUCCESS);
                validation.setMessage("User Updated");
            }catch (Exception e){
                log.error("Error updating user",e);
                validation.setStatus(Constants.FAILED);
                validation.setMessage("System Error");
            }


        }else {
            validation.setStatus(Constants.FAILED);
            validation.setMessage("User not Exists");
        }
        return validation;
    }
}
