package com.paymentProject.services;

import com.paymentProject.exceptions.AccountException;
import com.paymentProject.exceptions.TransactionException;
import com.paymentProject.exceptions.UserException;
import com.paymentProject.repositories.AccountRepository;
import com.paymentProject.repositories.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.paymentProject.utils.Utils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ExtendWith(SpringExtension.class)
class TransactionServiceTest {

    @Mock
    TransactionRepository transactionRepository;

    @Mock
    AccountRepository accountRepository;

    @Mock
    UserService userService;

    @Mock
    AccountService accountService;

    @InjectMocks
    TransactionService service;

    @Test
    void whenTryingToCreateTransaction_givenAnUserThatHasBalanceAndCanSendMoney_shouldCreateTransaction() throws UserException, AccountException {
        when(userService.getUserById(any())).thenReturn(getCommonUserEntity()).thenReturn(getSellerUserEntity());
        when(accountRepository.findAccountByUser(any())).thenReturn(Optional.ofNullable(getAccountEntity()));
        when(accountService.getAccountByUser(any())).thenReturn(getAccountEntity());
        when(transactionRepository.save(any())).thenReturn(getTransactionEntity());

        var response = assertDoesNotThrow(() -> service.createTransaction(getTransactionDTO()));
        assertNotNull(response);
        assertEquals(1L, response.getSendingUser().getId());
        assertEquals(2L, response.getReceivingUser().getId());
        assertEquals(BigDecimal.TEN, response.getValue());
    }

    @Test
    void whenTryingToCreateTransaction_givenAnUserThatHasNoBalance_shouldNotCreateTransaction() throws UserException, AccountException {
        when(userService.getUserById(any())).thenReturn(getCommonUserEntity()).thenReturn(getSellerUserEntity());
        doThrow(new UserException("User has no balance available", BAD_REQUEST))
                .when(userService).validateUserCanTransact(any(), any());

        verify(transactionRepository, times(0)).save(any());
    }

    @Test
    void whenTryingToCreateTransaction_givenAnUserIsNotAllowedToSendMoney_shouldNotCreateTransaction() throws UserException, AccountException {
        when(userService.getUserById(any())).thenReturn(getCommonUserEntity()).thenReturn(getSellerUserEntity());
        doThrow(new UserException("This user type can not transact sending money", BAD_REQUEST))
                .when(userService).validateUserCanTransact(any(), any());

        verify(transactionRepository, times(0)).save(any());
    }

    @Test
    void whenTryingToGetAllTransactionsByUserId_givenAnUserThatHasTransactions_thenShouldReturnTransactionsList() {
        when(transactionRepository.findAllBySendingUserIdOrReceivingUserId(any(), any()))
                .thenReturn(Optional.of(List.of(getTransactionEntity())));

        var response = assertDoesNotThrow(() -> service.getAllTransactionsByUserId(1L));
        assertEquals(1, response.size());
        assertEquals(BigDecimal.TEN, response.get(0).getValue());
    }

    @Test
    void whenTryingToGetAllTransactionsByUserId_givenAnUserThatHasNoTransactions_thenShouldThrowException() {
        when(transactionRepository.findAllBySendingUserIdOrReceivingUserId(any(), any()))
                .thenReturn(Optional.empty());

        var response = assertThrows(TransactionException.class, () -> service.getAllTransactionsByUserId(1L));
        assertEquals("Transactions not found", response.getMessage());
    }

    @Test
    void whenTryingToGetAllSendingTransactionsByUserId_givenAnUserThatHasTransactionAsSender_thenShouldReturnTransactionsList() {
        when(transactionRepository.findAllBySendingUserId(any()))
                .thenReturn(Optional.of(List.of(getTransactionEntity())));

        var response = assertDoesNotThrow(() -> service.getAllSendingTransactionsByUserId(1L));
        assertEquals(1, response.size());
        assertEquals(1L, response.get(0).getSendingUser().getId());
    }

    @Test
    void whenTryingToGetAllSendingTransactionsByUserId_givenAnUserThatDoesNotHaveTransactionAsSender_thenShouldThrowException() {
        when(transactionRepository.findAllBySendingUserId(any()))
                .thenReturn(Optional.empty());

        var response = assertThrows(TransactionException.class, () -> service.getAllSendingTransactionsByUserId(1L));
        assertEquals("Transactions not found", response.getMessage());
    }

    @Test
    void whenTryingToGetAllReceivingTransactionsByUserId_givenAnUserThatHasTransactionAsReceiver_thenShouldReturnTransactionsList() {
        when(transactionRepository.findAllByReceivingUserId(any()))
                .thenReturn(Optional.of(List.of(getTransactionEntity())));

        var response = assertDoesNotThrow(() -> service.getAllReceivingTransactionsByUserId(2L));
        assertEquals(1, response.size());
        assertEquals(2L, response.get(0).getReceivingUser().getId());
    }

    @Test
    void whenTryingToGetAllReceivingTransactionsByUserId_givenAnUserThatDoesNotHaveTransactionAsReceiver_thenShouldThrowException() {
        when(transactionRepository.findAllByReceivingUserId(any()))
                .thenReturn(Optional.empty());

        var response = assertThrows(TransactionException.class, () -> service.getAllReceivingTransactionsByUserId(1L));
        assertEquals("Transactions not found", response.getMessage());
    }
}