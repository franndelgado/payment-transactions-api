package com.franndelgado.payment_transactions_api.dto;

import java.math.BigDecimal;
import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.franndelgado.payment_transactions_api.enums.TransactionStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Transaction data transfer object")
public class TransactionDTO {

    @Schema(description = "Unique transaction identifier", 
            example = "51191953-669f-4fa7-af99-2fa7e96fbf8c")
    @JsonProperty("transaction_id")
    private String transactionId;

    @Schema(description = "User Identifier", 
            example = "113411")
    @JsonProperty("user_id")
    private String userId;

    @Schema(description = "Amount", 
            example = "250.00")
    private BigDecimal amount;

    @Schema(description = "Currency", 
            example = "EUR")
    private String currency;

    private TransactionStatus status;

    @JsonProperty("created_at")
    private Instant createdAt;

    @Schema(description = "Bank code", 
            example = "BANK123")
    @JsonProperty("bank_code")
    private String bankCode;

    @Schema(description = "Recipient account", 
            example = "DE89370400440532013000")
    @JsonProperty("recipient_account")
    private String recipientAccount;
}
