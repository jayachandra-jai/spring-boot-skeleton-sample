package com.jai.mystarter.services;

import com.jai.mystarter.models.auth.ERole;
import com.jai.mystarter.models.auth.Role;
import com.jai.mystarter.models.auth.User;
import com.jai.mystarter.models.dto.user.AddUser;
import com.jai.mystarter.repository.RoleRepository;
import com.jai.mystarter.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.HashSet;
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

    public User addUser(AddUser addUser,String userName){
        try {
            User user=new User();
            user.setUsername(addUser.getUsername());
            user.setPassword(userPasswordEncoder.encode(addUser.getPassword()));
            user.setPassword_updated_at(new Timestamp(System.currentTimeMillis()));
            user.setFull_name(addUser.getFull_name());
            user.setPhone(addUser.getPhone());
            user.setEmail(addUser.getEmail());
            user.setCreated_by(userName);
            Set<Role> roleSet=new HashSet<>();
            Role role=new Role();
            if(addUser.getRole().equals("SUPER_ADMIN")){
                role.setId(2);
                role.setName(ERole.ROLE_SUPER_ADMIN);
            }

            if(addUser.getRole().equals("ADMIN")){
                role.setId(1);
                role.setName(ERole.ROLE_ADMIN);
            }

            if(addUser.getRole().equals("USER")){
                role.setId(3);
                role.setName(ERole.ROLE_USER);
            }

            roleSet.add(role);
            user.setRoles(roleSet);
            return userRepository.save(user);
        }catch (Exception e){
           log.error("Error  Adding User",e);
        }
        return null;
    }

    public boolean isValidUser(AddUser addUser){
        if(null!=addUser){
            if(!StringUtils.isEmpty(addUser.getUsername())
                    && !StringUtils.isEmpty(addUser.getPassword())
                    && !StringUtils.isEmpty(addUser.getFull_name())
                    && !StringUtils.isEmpty(addUser.getRole()))
                return true;
        }
        return false;
    }
    public boolean addDummyUser(){
        try {
            User user=new User();
            user.setUsername("superAdmin@js.lv");
            user.setPassword_updated_at(new Timestamp(System.currentTimeMillis()));
            user.setPassword(userPasswordEncoder.encode("12345678"));
            user.setFull_name("Super Admin");
            Set<Role> roleSet=new HashSet<>();
            Role role=new Role();
            role.setId(2);
            role.setName(ERole.ROLE_SUPER_ADMIN);
            roleSet.add(role);
            user.setRoles(roleSet);
            userRepository.save(user);
            return true;
        }catch (Exception e){
            log.error("Error  Adding User",e);
        }
        return false;
    }
}
