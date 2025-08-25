package com.gtalent.tutor.repositories;

import com.gtalent.tutor.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    // select * from user where username = xxx;
    Optional<User> findByUsername(String username);

}
