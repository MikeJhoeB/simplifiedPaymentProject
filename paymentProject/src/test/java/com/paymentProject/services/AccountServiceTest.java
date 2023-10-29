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

import static com.paymentProject.utils.Utils.getAccount;
import static com.paymentProject.utils.Utils.getCommonUser;
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
    void createAccount() {
        ArgumentCaptor<Account> accountCaptor = ArgumentCaptor.forClass(Account.class);
        assertDoesNotThrow(() -> service.createAccount(getCommonUser()));

        verify(accountRepository, times(1)).save(accountCaptor.capture());
        Account capturedAccount = accountCaptor.getValue();
        assertNotNull(capturedAccount);
        assertEquals("João", capturedAccount.getUser().getFirstName());
        assertEquals(BigDecimal.TEN, capturedAccount.getBalance());

    }

    @Test
    void givenUserIdThatHasAnAccount_whenGetAccountByUserId_shouldReturnAccount() {
        when(accountRepository.findAccountByUserId(any())).thenReturn(Optional.ofNullable(getAccount()));

        var response = assertDoesNotThrow(() -> service.getAccountByUserId(1L));
        assertEquals(1L, response.getId());
        assertEquals(BigDecimal.TEN, response.getBalance());
    }

    @Test
    void givenUserIdThatHasNotAnAccount_whenGetAccountByUserId_thenShouldThrowException() {
        when(accountRepository.findAccountByUserId(anyLong())).thenReturn(Optional.empty());

        assertThrows(AccountException.class, () -> service.getAccountByUserId(1L));
    }

    @Test
    void givenUserThatHasAnAccount_whenGetAccountByUser_shouldReturnAccount() {

        when(accountRepository.findAccountByUser(any())).thenReturn(Optional.ofNullable(getAccount()));

        var response = assertDoesNotThrow(() -> service.getAccountByUser(getCommonUser()));
        assertEquals(1L, response.getId());
        assertEquals(BigDecimal.TEN, response.getBalance());
    }

    @Test
    void givenUserThatHasNotAnAccount_whenGetAccountByUser_thenShouldThrowException() {
        when(accountRepository.findAccountByUser(any())).thenReturn(Optional.empty());

        assertThrows(AccountException.class, () -> service.getAccountByUser(getCommonUser()));
    }

    @Test
    void debitValue() {
        ArgumentCaptor<Account> accountCaptor = ArgumentCaptor.forClass(Account.class);
        when(accountRepository.findAccountByUser(any())).thenReturn(Optional.ofNullable(getAccount()));

        assertDoesNotThrow(() -> service.debitValue(getCommonUser(), BigDecimal.TEN));
        verify(accountRepository, times(1)).save(accountCaptor.capture());

        Account capturedAccount = accountCaptor.getValue();
        assertNotNull(capturedAccount);
        assertEquals(BigDecimal.ZERO, capturedAccount.getBalance());
    }

    @Test
    void creditValue() {
        ArgumentCaptor<Account> accountCaptor = ArgumentCaptor.forClass(Account.class);
        when(accountRepository.findAccountByUser(any())).thenReturn(Optional.ofNullable(getAccount()));

        assertDoesNotThrow(() -> service.creditValue(getCommonUser(), BigDecimal.TEN));
        verify(accountRepository, times(1)).save(accountCaptor.capture());

        Account capturedAccount = accountCaptor.getValue();
        assertNotNull(capturedAccount);
        assertEquals(BigDecimal.valueOf(20L), capturedAccount.getBalance());
    }

}