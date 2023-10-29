package com.paymentProject.services;

import com.paymentProject.dtos.request.UserDTO;
import com.paymentProject.entities.User;
import com.paymentProject.enums.UserType;
import com.paymentProject.exceptions.AccountException;
import com.paymentProject.exceptions.UserException;
import com.paymentProject.repositories.UserRepository;
import org.springframework.http.HttpStatus;
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

    public User createUser(UserDTO newUser) throws UserException {
        var userExists = userRepository.findUserByDocumentOrEmail(newUser.document(), newUser.email());
        if (userExists.isPresent())
            throw new UserException("User already exists with this document or email", HttpStatus.CONFLICT);

        var userEntity = userDtoToEntity(newUser);
        var createdUser = userRepository.save(userEntity);
        accountService.createAccount(createdUser);
        return createdUser;
    }

    public User getUserById(Long userid) throws UserException {
        return userRepository.findById(userid)
                .orElseThrow(() -> new UserException("User not found", HttpStatus.NOT_FOUND));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void validateUserCanTransact(User user, BigDecimal value) throws AccountException, UserException {
        if (user.getUserType().equals(UserType.SELLER_USER))
            throw new UserException("This user type can not transact sending money", HttpStatus.BAD_REQUEST);

        var userAccount = accountService.getAccountByUser(user);

        if (userAccount.getBalance().compareTo(value) < 0)
            throw new UserException("User has no balance available", HttpStatus.BAD_REQUEST);

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
