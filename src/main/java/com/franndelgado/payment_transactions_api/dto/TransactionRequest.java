package com.franndelgado.payment_transactions_api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionRequest {

    @NotBlank(message = "User Id is missing.")
    private String userId;
}
