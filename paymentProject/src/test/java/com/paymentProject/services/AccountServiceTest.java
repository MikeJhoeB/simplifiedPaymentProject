package com.paymentProject.services;

import com.paymentProject.entities.Account;
import com.paymentProject.exceptions.AccountException;
import com.paymentProject.repositories.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static com.paymentProject.utils.Utils.getAccountEntity;
import static com.paymentProject.utils.Utils.getCommonUserEntity;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class AccountServiceTest {

    @Mock
    AccountRepository accountRepository;

    @InjectMocks
    AccountService service;

    @Test
    void givenValidUser_shouldCreateAccount() {
        ArgumentCaptor<Account> accountCaptor = ArgumentCaptor.forClass(Account.class);
        assertDoesNotThrow(() -> service.createAccount(getCommonUserEntity()));

        verify(accountRepository, times(1)).save(accountCaptor.capture());
        Account capturedAccount = accountCaptor.getValue();
        assertNotNull(capturedAccount);
        assertEquals("João", capturedAccount.getUser().getFirstName());
        assertEquals(BigDecimal.TEN, capturedAccount.getBalance());
    }

    @Test
    void givenUserIdThatHasAnAccount_whenGetAccountByUserId_shouldReturnAccount() {
        when(accountRepository.findAccountByUserId(any())).thenReturn(Optional.ofNullable(getAccountEntity()));

        var response = assertDoesNotThrow(() -> service.getAccountByUserId(1L));
        assertEquals(1L, response.getId());
        assertEquals(BigDecimal.TEN, response.getBalance());
    }

    @Test
    void givenUserIdThatHasNotAnAccount_whenGetAccountByUserId_thenShouldThrowException() {
        when(accountRepository.findAccountByUserId(anyLong())).thenReturn(Optional.empty());

        var response = assertThrows(AccountException.class, () -> service.getAccountByUserId(1L));
        assertNotNull(response);
        assertEquals("Account not found", response.getMessage());
    }

    @Test
    void givenUserThatHasAnAccount_whenGetAccountByUser_shouldReturnAccount() {

        when(accountRepository.findAccountByUser(any())).thenReturn(Optional.ofNullable(getAccountEntity()));

        var response = assertDoesNotThrow(() -> service.getAccountByUser(getCommonUserEntity()));
        assertEquals(1L, response.getId());
        assertEquals(BigDecimal.TEN, response.getBalance());
    }

    @Test
    void givenUserThatHasNotAnAccount_whenGetAccountByUser_thenShouldThrowException() {
        when(accountRepository.findAccountByUser(any())).thenReturn(Optional.empty());

        var response = assertThrows(AccountException.class, () -> service.getAccountByUser(getCommonUserEntity()));
        assertNotNull(response);
        assertEquals("Account not found", response.getMessage());
    }

    @Test
    void givenValidAccount_shouldDebitValue() {
        ArgumentCaptor<Account> accountCaptor = ArgumentCaptor.forClass(Account.class);
        when(accountRepository.findAccountByUser(any())).thenReturn(Optional.ofNullable(getAccountEntity()));

        assertDoesNotThrow(() -> service.debitValue(getCommonUserEntity(), BigDecimal.TEN));
        verify(accountRepository, times(1)).save(accountCaptor.capture());

        Account capturedAccount = accountCaptor.getValue();
        assertNotNull(capturedAccount);
        assertEquals(BigDecimal.ZERO, capturedAccount.getBalance());
    }

    @Test
    void givenValidAccount_shouldCreditValue() {
        ArgumentCaptor<Account> accountCaptor = ArgumentCaptor.forClass(Account.class);
        when(accountRepository.findAccountByUser(any())).thenReturn(Optional.ofNullable(getAccountEntity()));

        assertDoesNotThrow(() -> service.creditValue(getCommonUserEntity(), BigDecimal.TEN));
        verify(accountRepository, times(1)).save(accountCaptor.capture());

        Account capturedAccount = accountCaptor.getValue();
        assertNotNull(capturedAccount);
        assertEquals(BigDecimal.valueOf(20L), capturedAccount.getBalance());
    }

    @Test
    void givenInvalidAccount_whenTryToDebitValue_shouldThrowNotFoundException() {
        when(accountRepository.findAccountByUser(any())).thenReturn(Optional.empty());

        var response = assertThrows(AccountException.class, () -> service.debitValue(getCommonUserEntity(), BigDecimal.TEN));

        assertNotNull(response);
        assertEquals("Account not found", response.getMessage());
    }

    @Test
    void givenInvalidAccount_whenTryToCreditValue_shouldThrowNotFoundException() {
        when(accountRepository.findAccountByUser(any())).thenReturn(Optional.empty());

        var response = assertThrows(AccountException.class, () -> service.creditValue(getCommonUserEntity(), BigDecimal.TEN));

        assertNotNull(response);
        assertEquals("Account not found", response.getMessage());
    }

}