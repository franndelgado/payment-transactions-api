package com.franndelgado.payment_transactions_api.entity;

import java.math.BigDecimal;
import java.time.Instant ;
import java.util.UUID;

import com.franndelgado.payment_transactions_api.enums.TransactionStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    private String transactionId = UUID.randomUUID().toString();

    private String userId;

    private BigDecimal amount;

    private String currency;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    private Instant createdAt;

    private String bankCode;

    private String recipientAccount;
}
