package com.franndelgado.payment_transactions_api.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.franndelgado.payment_transactions_api.dto.TransactionResponseDTO;
import com.franndelgado.payment_transactions_api.enums.TransactionStatus;

import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import io.swagger.v3.oas.annotations.Parameter;

import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "Transactions", description = "API for managing financial transactions")
public interface TransactionApi {

     @Operation(summary = "Create new transaction", 
               description = "Create a new transfer transaction.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Transaction created successfully"),
        @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    ResponseEntity<TransactionResponseDTO> createTransaction(
            @Parameter(description = "Transaction data to create") 
            @RequestBody TransactionResponseDTO transactionDTO);

    @Operation(summary = "Check the status of a transaction", 
               description = "Check the status of a transaction by the Transaction Id.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Status successfully obtained"),
        @ApiResponse(responseCode = "404", description = "Not found")
    })       
    ResponseEntity<TransactionStatus> getTransactionStatus(
        @Parameter(description = "Unique transaction identifier", example = "123e4567-e89b-12d3-a456-426614174000")
        @PathVariable String transactionId);

    @Operation(summary = "List transactions approved by user", 
               description = "List transactions approved by user Id.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully obtained"),
        @ApiResponse(responseCode = "404", description = "Not found")
    }) 
    ResponseEntity<List<TransactionResponseDTO>> getApprovedTransactionsByUserId(@RequestParam String userId);
}
