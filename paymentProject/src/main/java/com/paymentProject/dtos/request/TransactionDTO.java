package com.paymentProject.dtos.request;

import java.math.BigDecimal;

public record TransactionDTO(BigDecimal value,
                             Long sendingUserId,
                             Long receivingUserId) {
}
