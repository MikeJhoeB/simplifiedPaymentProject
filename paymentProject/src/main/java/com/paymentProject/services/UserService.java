package com.paymentProject.services;

import com.paymentProject.entities.User;
import com.paymentProject.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User getUserById(Long userid) throws Exception {
        return Optional.of(userRepository.findById(userid)).get()
                .orElseThrow(() -> new Exception("Usuário não encontrado"));
    }

}
