package com.jai.mystarter.repository;

import com.jai.mystarter.models.auth.ERole;
import com.jai.mystarter.models.auth.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(ERole name);
}
