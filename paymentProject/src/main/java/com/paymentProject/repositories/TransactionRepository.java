package com.paymentProject.repositories;

import com.paymentProject.entities.Transaction;
import com.paymentProject.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Optional<List<Transaction>> findAllBySendingUserOrReceivingUser(User sendingUser, User receivingUser);
    Optional<List<Transaction>> findAllBySendingUser(User sendingUser);
    Optional<List<Transaction>> findAllByReceivingUser(User receivingUser);
}
