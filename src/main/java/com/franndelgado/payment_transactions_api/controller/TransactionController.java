package com.franndelgado.payment_transactions_api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.franndelgado.payment_transactions_api.dto.TransactionDTO;
import com.franndelgado.payment_transactions_api.enums.TransactionStatus;
import com.franndelgado.payment_transactions_api.service.TransactionService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
   
    }

    @PostMapping
    public ResponseEntity<TransactionDTO> createTransaction(@RequestBody TransactionDTO transactionDTO) {
        TransactionDTO createdTransaction = transactionService.createTransaction(transactionDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTransaction);
    }
    
    @GetMapping("/status/{transaction_id}")
    public ResponseEntity<TransactionStatus> getTransactionStatus(@PathVariable String transaction_id) {
        TransactionStatus status = transactionService.getTransactionStatus(transaction_id);
        return ResponseEntity.ok(status);
    }
    
    @GetMapping("/approved")
    public ResponseEntity<List<TransactionDTO>> getApprovedTransactionsByUserId(@RequestParam String user_id) {
        List<TransactionDTO> transactions = transactionService.getApprovedTransactionsByUserId(user_id);
        return ResponseEntity.ok(transactions);
    }
    
}
