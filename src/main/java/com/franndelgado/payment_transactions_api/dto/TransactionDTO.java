package com.franndelgado.payment_transactions_api.dto;

import java.math.BigDecimal;
import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.franndelgado.payment_transactions_api.enums.TransactionStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDTO {

    @JsonProperty("transaction_id")
    private String transactionId;

    @JsonProperty("user_id")
    private String userId;

    private BigDecimal amount;

    private String currency;

    private TransactionStatus status;

    @JsonProperty("created_at")
    private Instant createdAt;

    @JsonProperty("bank_code")
    private String bankCode;

    @JsonProperty("recipient_account")
    private String recipientAccount;
}
