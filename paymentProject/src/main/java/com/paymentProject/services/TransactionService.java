package com.paymentProject.services;

import com.paymentProject.dtos.request.TransactionDTO;
import com.paymentProject.entities.Transaction;
import com.paymentProject.entities.User;
import com.paymentProject.exceptions.AccountException;
import com.paymentProject.exceptions.TransactionException;
import com.paymentProject.exceptions.UserException;
import com.paymentProject.repositories.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserService userService;
    private final AccountService accountService;

    public TransactionService(TransactionRepository transactionRepository,
                              UserService userService,
                              AccountService accountService) {
        this.transactionRepository = transactionRepository;
        this.userService = userService;
        this.accountService = accountService;
    }

    @Transactional(rollbackOn = Exception.class)
    public Transaction createTransaction(TransactionDTO transactionRequest) throws UserException, AccountException {
        var transactionValue = transactionRequest.value();
        var sendingUser = userService.getUserById(transactionRequest.sendingUserId());

        userService.validateUserCanTransact(sendingUser, transactionValue);

        var receivingUser = userService.getUserById(transactionRequest.receivingUserId());

        var transaction = buildTransaction(transactionRequest, sendingUser, receivingUser);
        debitAndCreditTransactionValue(sendingUser, transactionValue, receivingUser);

        return transactionRepository.save(transaction);
    }

    private void debitAndCreditTransactionValue(User sendingUser, BigDecimal transactionValue, User receivingUser) throws AccountException {
        accountService.debitValue(sendingUser, transactionValue);
        accountService.creditValue(receivingUser, transactionValue);
    }

    private static Transaction buildTransaction(TransactionDTO transactionRequest,
                                                User sendingUser, User receivingUser) {
        return Transaction.builder()
                .sendingUser(sendingUser)
                .receivingUser(receivingUser)
                .value(transactionRequest.value())
                .build();
    }

    public List<Transaction> getAllTransactionsByUserId(Long userId) throws TransactionException {
        return transactionRepository.findAllBySendingUserIdOrReceivingUserId(userId, userId)
                .orElseThrow(() -> new TransactionException("Transactions not found", HttpStatus.NOT_FOUND));
    }

    public List<Transaction> getAllSendingTransactionsByUserId(Long userId) throws TransactionException {
        return transactionRepository.findAllBySendingUserId(userId)
                .orElseThrow(() -> new TransactionException("Transactions not found", HttpStatus.NOT_FOUND));
    }

    public List<Transaction> getAllReceivingTransactionsByUserId(Long userId) throws TransactionException {
        return transactionRepository.findAllByReceivingUserId(userId)
                .orElseThrow(() -> new TransactionException("Transactions not found", HttpStatus.NOT_FOUND));
    }
}
