package com.paymentProject.services;

import com.paymentProject.dtos.TransactionDTO;
import com.paymentProject.entities.Transaction;
import com.paymentProject.entities.User;
import com.paymentProject.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserService userService;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository,
                              UserService userService) {
        this.transactionRepository = transactionRepository;
        this.userService = userService;
    }

    public Transaction createTransaction(TransactionDTO transactionRequest) throws Exception {
        var sendingUser = userService.getUserById(transactionRequest.sendingUserId());

        userService.validateUserCanTransact(sendingUser, transactionRequest.value());

        var receivingUser = userService.getUserById(transactionRequest.receivingUserId());

        var transaction = buildTransaction(transactionRequest, sendingUser, receivingUser);

        return transactionRepository.save(transaction);
    }

    public Transaction getTransactionById(Long transactionId) throws Exception {
        return transactionRepository.findById(transactionId)
                .orElseThrow(() -> new Exception("Transaction not found"));
    }

    public List<Transaction> getAllTransactionsByUser(User user) throws Exception {
        return transactionRepository.findAllBySendingUserOrReceivingUser(user, user)
                .orElseThrow(() -> new Exception("Transactions not found"));
    }

    public List<Transaction> getAllSendingTransactionsByUser(User user) throws Exception {
        return transactionRepository.findAllBySendingUser(user)
                .orElseThrow(() -> new Exception("Transactions not found"));
    }

    public List<Transaction> getAllReceivingTransactionsByUser(User user) throws Exception {
        return transactionRepository.findAllByReceivingUser(user)
                .orElseThrow(() -> new Exception("Transactions not found"));
    }

    private static Transaction buildTransaction(TransactionDTO transactionRequest,
                                                User sendingUser, User receivingUser) {
        return Transaction.builder()
                .sendingUser(sendingUser)
                .receivingUser(receivingUser)
                .value(transactionRequest.value())
                .build();
    }
}
