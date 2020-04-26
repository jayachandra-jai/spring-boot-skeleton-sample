package com.jai.mystarter.repository;

import com.jai.mystarter.models.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(@Param("username") String username);

}
