package com.paymentProject.dtos;

import java.math.BigDecimal;

public record TransactionDTO(BigDecimal value,
                             Long sendingUserId,
                             Long receivingUserId) {
}
