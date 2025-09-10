package com.franndelgado.payment_transactions_api.entity;

import java.math.BigDecimal;
import java.time.Instant ;

import com.franndelgado.payment_transactions_api.enums.TransactionStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "transactions")
@Getter
@Setter
public class Transaction {

    @Id
    private String transaction_id;
    private String user_id;
    private BigDecimal amount;
    private String currency;
    private TransactionStatus status;
    private Instant created_at;
    private String bank_code;
    private String recipient_account;
}
