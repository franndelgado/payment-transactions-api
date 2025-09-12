package com.franndelgado.payment_transactions_api.service;

import java.time.Instant;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.franndelgado.payment_transactions_api.dto.TransactionDTO;
import com.franndelgado.payment_transactions_api.entity.Transaction;
import com.franndelgado.payment_transactions_api.enums.TransactionStatus;
import com.franndelgado.payment_transactions_api.exceptions.TransactionIdNotFoundException;
import com.franndelgado.payment_transactions_api.exceptions.TransactionUserIdNotFoundException;
import com.franndelgado.payment_transactions_api.repository.TransactionRepository;

import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    @Transactional
    public TransactionDTO createTransaction(TransactionDTO transactionDTO) {

        Transaction newTransaction = new Transaction();
        
        newTransaction.setUserId(transactionDTO.getUserId());
        newTransaction.setAmount(transactionDTO.getAmount());
        newTransaction.setCurrency(transactionDTO.getCurrency());
        newTransaction.setStatus(TransactionStatus.values()[new Random().nextInt(TransactionStatus.values().length)]);
        newTransaction.setCreatedAt(Instant.now());
        newTransaction.setBankCode(transactionDTO.getBankCode());
        newTransaction.setRecipientAccount(transactionDTO.getRecipientAccount());

        Transaction savedTransaction = transactionRepository.save(newTransaction);

        return mapToDTO(savedTransaction);
    }

    @Override
    public TransactionStatus getTransactionStatus(String transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
        .orElseThrow(() -> new TransactionIdNotFoundException(transactionId));

        return transaction.getStatus();
    }

    @Override
    public List<TransactionDTO> getApprovedTransactionsByUserId(String userId) {

        if(userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("User Id is missing.");
        }

        List<Transaction> transactions = transactionRepository.findByUserIdAndStatus(userId, TransactionStatus.APPROVED);
        
        if (transactions.isEmpty()) {
            throw new TransactionUserIdNotFoundException(userId);
        }
           
        return transactions.stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    private TransactionDTO mapToDTO(Transaction transaction) {
        TransactionDTO dto = new TransactionDTO();
        dto.setTransactionId(transaction.getTransactionId());
        dto.setUserId(transaction.getUserId());
        dto.setAmount(transaction.getAmount());
        dto.setCurrency(transaction.getCurrency());
        dto.setStatus(transaction.getStatus());
        dto.setCreatedAt(transaction.getCreatedAt());
        dto.setBankCode(transaction.getBankCode());
        dto.setRecipientAccount(transaction.getRecipientAccount());
        return dto;
    }
}
