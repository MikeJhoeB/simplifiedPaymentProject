package com.paymentProject.repositories;

import com.paymentProject.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Optional<List<Transaction>> findAllBySendingUserIdOrReceivingUserId(Long sendingUserId, Long receivingUserId);
    Optional<List<Transaction>> findAllBySendingUserId(Long sendingUserId);
    Optional<List<Transaction>> findAllByReceivingUserId(Long receivingUserId);
}
