package com.paymentProject.services;

import com.paymentProject.entities.User;
import com.paymentProject.exceptions.AccountException;
import com.paymentProject.exceptions.UserException;
import com.paymentProject.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.paymentProject.utils.Utils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    AccountService accountService;

    @InjectMocks
    UserService service;

    @Test
    void whenTryingToCreateUser_givenValidRequest_thenShouldCreateWithSuccess() {
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        doNothing().when(accountService).createAccount(any());

        assertDoesNotThrow(() -> service.createUser(getCommonUserDTO()));
        verify(userRepository, times(1)).save(userCaptor.capture());
        User capturedUser = userCaptor.getValue();
        assertNotNull(capturedUser);
        assertEquals("João", capturedUser.getFirstName());
    }

    @Test
    void whenTryingToCreateUser_givenAlreadyRegisteredEmailOrDocument_thenShouldThrowException() {
        when(userRepository.findUserByDocumentOrEmail(any(), any())).thenReturn(Optional.ofNullable(getCommonUserEntity()));

        var response = assertThrows(UserException.class, () -> service.createUser(getCommonUserDTO()));
        verify(accountService, times(0)).createAccount(any());
        verify(userRepository, times(0)).save(any());
        assertNotNull(response);
        assertEquals("User already exists with this document or email", response.getMessage());
    }

    @Test
    void whenTryingToGetUserById_givenUserIdThatExists_thenShouldReturnUser() {
        when(userRepository.findById(any())).thenReturn(Optional.ofNullable(getCommonUserEntity()));

        var response = assertDoesNotThrow(() -> service.getUserById(1L));
        assertNotNull(response);
        assertEquals(1L, response.getId());
    }

    @Test
    void whenTryingToGetUserById_givenUserIdThatDoesNotExists_thenShouldThrowException() {
        when(userRepository.findById(any())).thenReturn(Optional.empty());

        var response = assertThrows(UserException.class, () -> service.getUserById(1L));
        assertNotNull(response);
        assertEquals("User not found", response.getMessage());
    }

    @Test
    void whenTryingToGetAllUsers_withAnUserCreated_thenShouldReturnListOfUsers() {
        when(userRepository.findAll()).thenReturn(List.of(getCommonUserEntity()));

        var response = assertDoesNotThrow(() -> service.getAllUsers());
        assertEquals(1, response.size());
        assertEquals("João", response.get(0).getFirstName());
    }

    @Test
    void whenTryingToGetAllUsers_withNoUserCreated_thenShouldReturnEmptyList() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        var response = assertDoesNotThrow(() -> service.getAllUsers());
        assertEquals(0, response.size());
    }

    @Test
    void whenTryingToValidateUserCanTransact_whereUserIsCommonUserAndHasBalance_thenShouldAllowTransaction() throws AccountException {
        when(accountService.getAccountByUser(any())).thenReturn(getAccountEntity());

        assertDoesNotThrow(() -> service.validateUserCanTransact(getCommonUserEntity(), BigDecimal.TEN));
    }

    @Test
    void whenTryingToValidateUserCanTransact_whereUserIsCommonUserButHasNoBalance_thenShouldNotAllowTransaction() throws AccountException {
        var userAccount = getAccountEntity();
        userAccount.setBalance(BigDecimal.ZERO);
        when(accountService.getAccountByUser(any())).thenReturn(userAccount);

        var response = assertThrows(UserException.class,
                () -> service.validateUserCanTransact(getCommonUserEntity(), BigDecimal.TEN));
        assertEquals("User has no balance available", response.getMessage());
    }

    @Test
    void whenTryingToValidateUserCanTransact_whereUserIsSellerUser_thenShouldNotAllowTransaction() {
        var response = assertThrows(UserException.class,
                () -> service.validateUserCanTransact(getSellerUserEntity(), BigDecimal.TEN));
        assertEquals("This user type can not transact sending money", response.getMessage());
    }
}