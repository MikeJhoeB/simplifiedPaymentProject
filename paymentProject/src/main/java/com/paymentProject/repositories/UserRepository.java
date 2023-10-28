package com.paymentProject.repositories;

import com.paymentProject.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByDocumentOrEmail(String document, String email);
    
}
