package com.paymentProject.utils;

import com.paymentProject.entities.Account;
import com.paymentProject.entities.User;
import com.paymentProject.enums.UserType;

import java.math.BigDecimal;

public class Utils {

    public static Account getAccount() {
        return Account.builder()
                .id(1L)
                .user(getCommonUser())
                .balance(BigDecimal.TEN)
                .build();
    }

    public static User getCommonUser() {
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
}
