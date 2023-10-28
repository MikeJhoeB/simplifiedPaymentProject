package com.paymentProject.services;

import com.paymentProject.entities.User;
import com.paymentProject.enums.UserType;
import com.paymentProject.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final AccountService accountService;

    @Autowired
    public UserService(UserRepository userRepository,
                       AccountService accountService) {
        this.userRepository = userRepository;
        this.accountService = accountService;
    }

    public User createUser(User user) {
        var createdUser = userRepository.save(user);
        accountService.createAccount(createdUser);
        return createdUser;
    }

    public User getUserById(Long userid) throws Exception {
        return Optional.of(userRepository.findById(userid)).get()
                .orElseThrow(() -> new Exception("Usuário não encontrado"));
    }

    public boolean validateUserCanTransact(User user, BigDecimal value) throws Exception {
        var userAccount = accountService.getAccountByUser(user);
        if (user.getUserType().equals(UserType.SELLER_USER))
            throw new Exception("This user type can not transact sending money");

        if (userAccount.getBalance().compareTo(value) < 0)
            throw new Exception("User has no balance available");

        return true;
    }

}
