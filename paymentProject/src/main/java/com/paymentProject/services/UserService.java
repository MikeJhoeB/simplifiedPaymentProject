package com.paymentProject.services;

import com.paymentProject.dtos.UserDTO;
import com.paymentProject.entities.User;
import com.paymentProject.enums.UserType;
import com.paymentProject.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final AccountService accountService;

    public UserService(UserRepository userRepository,
                       AccountService accountService) {
        this.userRepository = userRepository;
        this.accountService = accountService;
    }

    public User createUser(UserDTO newUser) {
        var userEntity = userDtoToEntity(newUser);
        var createdUser = userRepository.save(userEntity);
        accountService.createAccount(createdUser);
        return createdUser;
    }

    public User getUserById(Long userid) throws Exception {
        return userRepository.findById(userid)
                .orElseThrow(() -> new Exception("Usuário não encontrado"));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void validateUserCanTransact(User user, BigDecimal value) throws Exception {
        var userAccount = accountService.getAccountByUser(user);
        if (user.getUserType().equals(UserType.SELLER_USER))
            throw new Exception("This user type can not transact sending money");

        if (userAccount.getBalance().compareTo(value) < 0)
            throw new Exception("User has no balance available");

    }

    private static User userDtoToEntity(UserDTO newUser) {
        return User.builder()
                .firstName(newUser.firstName())
                .lastName(newUser.lastName())
                .document(newUser.document())
                .email(newUser.email())
                .password(newUser.password())
                .userType(newUser.userType())
                .build();
    }

}
