package com.paymentProject.utils;

import com.paymentProject.dtos.request.TransactionDTO;
import com.paymentProject.dtos.request.UserDTO;
import com.paymentProject.entities.Account;
import com.paymentProject.entities.Transaction;
import com.paymentProject.entities.User;
import com.paymentProject.enums.UserType;

import java.math.BigDecimal;

public class Utils {

    public static Account getAccountEntity() {
        return Account.builder()
                .id(1L)
                .user(getCommonUserEntity())
                .balance(BigDecimal.TEN)
                .build();
    }

    public static User getCommonUserEntity() {
        return User.builder()
                .id(1L)
                .firstName("João")
                .lastName("Almeida")
                .document("11111111111")
                .email("joao@gmail.com")
                .password("123")
                .userType(UserType.COMMON_USER)
                .build();
    }

    public static User getSellerUserEntity() {
        return User.builder()
                .id(2L)
                .firstName("Almeida")
                .lastName("Almeida")
                .document("11111111111")
                .email("joao@gmail.com")
                .password("123")
                .userType(UserType.SELLER_USER)
                .build();
    }

    public static Transaction getTransactionEntity() {
        return Transaction.builder()
                .id(1L)
                .sendingUser(getCommonUserEntity())
                .receivingUser(getSellerUserEntity())
                .value(BigDecimal.TEN)
                .build();
    }

    public static UserDTO getCommonUserDTO() {
        return UserDTO.builder()
                .firstName("João")
                .lastName("Almeida")
                .document("11111111111")
                .email("joao@gmail.com")
                .password("123")
                .userType(UserType.COMMON_USER)
                .build();
    }

    public static TransactionDTO getTransactionDTO() {
        return TransactionDTO.builder()
                .sendingUserId(1L)
                .receivingUserId(2L)
                .value(BigDecimal.TEN)
                .build();
    }
}
