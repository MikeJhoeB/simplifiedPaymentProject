package com.paymentProject.dtos.request;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record TransactionDTO(BigDecimal value,
                             Long sendingUserId,
                             Long receivingUserId) {
}
