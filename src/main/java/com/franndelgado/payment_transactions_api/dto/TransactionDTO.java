package com.franndelgado.payment_transactions_api.dto;

import java.math.BigDecimal;
import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.franndelgado.payment_transactions_api.enums.TransactionStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @NotBlank(message = "User ID cannot be empty")
    @JsonProperty("user_id")
    private String userId;

    @Schema(description = "Amount", 
            example = "250.00")
    @NotNull(message = "Amount is required")
    @Min(value = 1, message = "The amount cannot be zero or negative")
    private BigDecimal amount;

    @Schema(description = "Currency", 
            example = "EUR")
    @NotBlank(message = "Currency is required")
    private String currency;

    private TransactionStatus status;

    @JsonProperty("created_at")
    private Instant createdAt;

    @Schema(description = "Bank code", 
            example = "BANK123")
    @NotBlank(message = "Bank code is required")
    @JsonProperty("bank_code")
    private String bankCode;

    @Schema(description = "Recipient account", 
            example = "DE89370400440532013000")
    @NotBlank(message = "Recipient account is required")
    @JsonProperty("recipient_account")
    private String recipientAccount;
}
