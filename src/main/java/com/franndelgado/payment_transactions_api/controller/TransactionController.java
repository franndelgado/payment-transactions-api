package com.franndelgado.payment_transactions_api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.franndelgado.payment_transactions_api.dto.TransactionDTO;
import com.franndelgado.payment_transactions_api.dto.TransactionRequest;
import com.franndelgado.payment_transactions_api.enums.TransactionStatus;
import com.franndelgado.payment_transactions_api.service.TransactionService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<TransactionDTO> createTransaction(@Valid @RequestBody TransactionDTO transactionDTO) {
        TransactionDTO createdTransaction = transactionService.createTransaction(transactionDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTransaction);
    }
    
    @GetMapping("/status/{transactionId}")
    public ResponseEntity<TransactionStatus> getTransactionStatus(@Valid @PathVariable String transactionId) {
        TransactionStatus status = transactionService.getTransactionStatus(transactionId);
        return ResponseEntity.status(HttpStatus.OK).body(status);
    }
    
    @GetMapping("/approved")
    public ResponseEntity<List<TransactionDTO>> getApprovedTransactionsByUserId(@Valid TransactionRequest transactionRequest) {
        List<TransactionDTO> transactions = transactionService.getApprovedTransactionsByUserId(transactionRequest.getUserId());
        return ResponseEntity.status(HttpStatus.OK).body(transactions);
    }
}
