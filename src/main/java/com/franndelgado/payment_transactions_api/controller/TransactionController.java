package com.franndelgado.payment_transactions_api.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.franndelgado.payment_transactions_api.dto.TransactionResponseDTO;
import com.franndelgado.payment_transactions_api.dto.TransactionRequest;
import com.franndelgado.payment_transactions_api.dto.TransactionRequestDTO;
import com.franndelgado.payment_transactions_api.enums.TransactionStatus;
import com.franndelgado.payment_transactions_api.service.TransactionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/transactions")
@Validated
@Tag(name = "Transactions", description = "API for managing financial transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Operation(summary = "Create new transaction", 
               description = "Create a new transfer transaction.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Transaction created successfully"),
        @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @PostMapping
    public ResponseEntity<TransactionResponseDTO> createTransaction(@Valid @RequestBody TransactionRequestDTO transactionDTO) {
        TransactionResponseDTO createdTransaction = transactionService.createTransaction(transactionDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTransaction);
    }
    
    @Operation(summary = "Check the status of a transaction", 
               description = "Check the status of a transaction by the Transaction Id.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Status successfully obtained"),
        @ApiResponse(responseCode = "404", description = "Not found")
    })   
    @GetMapping("/{transactionId}/status")
    public ResponseEntity<TransactionStatus> getTransactionStatus(@PathVariable @Pattern(regexp = "^[0-9a-fA-F-]{36}$", message = "Transaction ID must be a valid UUID") String transactionId) {
        TransactionStatus status = transactionService.getTransactionStatus(transactionId);
        return ResponseEntity.ok(status);
    }
    
    @Operation(summary = "List transactions approved by user", 
               description = "List transactions approved by user Id.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully obtained"),
        @ApiResponse(responseCode = "404", description = "Not found")
    })
    @GetMapping("/approved")
    public ResponseEntity<Page<TransactionResponseDTO>> getApprovedTransactionsByUserId(@Valid TransactionRequest transactionRequest, 
                                                                                        @RequestParam(defaultValue = "0") int page,
                                                                                        @RequestParam(defaultValue = "10") int size,
                                                                                        @RequestParam(defaultValue = "createdAt,desc") String sort) {
        Page<TransactionResponseDTO> transactions = transactionService.getApprovedTransactionsByUserId(transactionRequest.getUserId(), page, size, sort);
        return ResponseEntity.status(HttpStatus.OK).body(transactions);
    }
}
