package com.franndelgado.payment_transactions_api.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Transaction data transfer object for request")
public class TransactionRequestDTO {
    
    @Schema(description = "User Identifier", 
            example = "113411")
    @NotBlank(message = "User ID cannot be empty.")
    @JsonProperty("user_id")
    private String userId;

    @Schema(description = "Amount", 
            example = "250.00")
    @NotNull(message = "Amount is required.")
    @DecimalMin(value = "1.00", inclusive = true, message = "The amount cannot be zero or negative.")
    private BigDecimal amount;

    @Schema(description = "Currency", 
            example = "EUR")
    @NotBlank(message = "Currency is required.")
    @Size(min = 3, max = 3, message = "Currency must be exactly 3 letters.")
    private String currency;

    @Schema(description = "Bank code", 
            example = "BANK123")
    @NotBlank(message = "Bank code is required.")
    @JsonProperty("bank_code")
    private String bankCode;

    @Schema(description = "Recipient account", 
            example = "DE89370400440532013000")
    @NotBlank(message = "Recipient account is required.")
    @JsonProperty("recipient_account")
    private String recipientAccount;
}
