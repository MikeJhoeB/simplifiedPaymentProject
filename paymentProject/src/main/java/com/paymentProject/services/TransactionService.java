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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserService userService;
    private final AccountService accountService;
    private final RestTemplate restTemplate;

    public TransactionService(TransactionRepository transactionRepository,
                              UserService userService,
                              AccountService accountService,
                              RestTemplate restTemplate) {
        this.transactionRepository = transactionRepository;
        this.userService = userService;
        this.accountService = accountService;
        this.restTemplate = restTemplate;
    }

    @Transactional(rollbackOn = Exception.class)
    public Transaction createTransaction(TransactionDTO transactionRequest) throws TransactionException, UserException, AccountException {
        var transactionValue = transactionRequest.value();
        var sendingUser = userService.getUserById(transactionRequest.sendingUserId());

        userService.validateUserCanTransact(sendingUser, transactionValue);

        var receivingUser = userService.getUserById(transactionRequest.receivingUserId());

        var transaction = buildTransaction(transactionRequest, sendingUser, receivingUser);
        debitAndCreditTransactionValue(sendingUser, transactionValue, receivingUser);

//        if (!validateTransactionExternally())
//            throw new TransactionException("Transaction was not authorized by external sources", HttpStatus.INTERNAL_SERVER_ERROR);

        return transactionRepository.save(transaction);
    }

    private void debitAndCreditTransactionValue(User sendingUser, BigDecimal transactionValue, User receivingUser) throws AccountException {
        accountService.debitValue(sendingUser, transactionValue);
        accountService.creditValue(receivingUser, transactionValue);
    }

    private boolean validateTransactionExternally() {
        ResponseEntity<Map> authorizedResponse = restTemplate.getForEntity("https://run.mocky.io/v3/8fafdd68-a090-496f-8c9a-3442cf30dae6", Map.class);

        if (authorizedResponse.getStatusCode().equals(HttpStatus.OK)) {
            String message = (String) Objects.requireNonNull(authorizedResponse.getBody()).get("message");
            return "Autorizado".equalsIgnoreCase(message);
        }

        return false;
    }

    private static Transaction buildTransaction(TransactionDTO transactionRequest,
                                                User sendingUser, User receivingUser) {
        return Transaction.builder()
                .sendingUser(sendingUser)
                .receivingUser(receivingUser)
                .value(transactionRequest.value())
                .build();
    }

    public Transaction getTransactionById(Long transactionId) throws TransactionException {
        return transactionRepository.findById(transactionId)
                .orElseThrow(() -> new TransactionException("Transaction not found", HttpStatus.NOT_FOUND));
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
