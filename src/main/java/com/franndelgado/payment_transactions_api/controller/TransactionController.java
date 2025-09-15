package com.franndelgado.payment_transactions_api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.franndelgado.payment_transactions_api.dto.TransactionResponseDTO;
import com.franndelgado.payment_transactions_api.dto.TransactionRequest;
import com.franndelgado.payment_transactions_api.dto.TransactionRequestDTO;
import com.franndelgado.payment_transactions_api.enums.TransactionStatus;
import com.franndelgado.payment_transactions_api.service.TransactionService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/transactions")
@Validated
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<TransactionResponseDTO> createTransaction(@Valid @RequestBody TransactionRequestDTO transactionDTO) {
        TransactionResponseDTO createdTransaction = transactionService.createTransaction(transactionDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTransaction);
    }
    
    @GetMapping("/status/{transactionId}")
    public ResponseEntity<TransactionStatus> getTransactionStatus(@PathVariable String transactionId) {
        TransactionStatus status = transactionService.getTransactionStatus(transactionId);
        return ResponseEntity.ok(status);
    }
    
    @GetMapping("/approved")
    public ResponseEntity<List<TransactionResponseDTO>> getApprovedTransactionsByUserId(@Valid TransactionRequest transactionRequest) {
        List<TransactionResponseDTO> transactions = transactionService.getApprovedTransactionsByUserId(transactionRequest.getUserId());
        return ResponseEntity.status(HttpStatus.OK).body(transactions);
    }
}
