package com.franndelgado.payment_transactions_api.dto;

import java.math.BigDecimal;
import java.time.Instant;

import com.franndelgado.payment_transactions_api.enums.TransactionStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TransactionDTO {

    private String transaction_id;
    private String user_id;
    private BigDecimal amount;
    private String currency;
    private TransactionStatus status;
    private Instant created_at;
    private String bank_code;
    private String recipient_account;
}
