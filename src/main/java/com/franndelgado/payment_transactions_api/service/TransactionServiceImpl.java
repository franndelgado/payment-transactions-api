package com.franndelgado.payment_transactions_api.service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.franndelgado.payment_transactions_api.dto.TransactionDTO;
import com.franndelgado.payment_transactions_api.entity.Transaction;
import com.franndelgado.payment_transactions_api.enums.TransactionStatus;
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

        return new TransactionDTO(
            savedTransaction.getTransactionId(),
            savedTransaction.getUserId(),
            savedTransaction.getAmount(),
            savedTransaction.getCurrency(),
            savedTransaction.getStatus(),
            savedTransaction.getCreatedAt(),
            savedTransaction.getBankCode(),
            savedTransaction.getRecipientAccount());
    }

    @Override
    public TransactionStatus getTransactionStatus(String transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
        .orElseThrow(() -> new NoSuchElementException("Transaction Id: " + transactionId + " not exist."));
        
        return transaction.getStatus();
    }

    @Override
    public List<TransactionDTO> getApprovedTransactionsByUserId(String userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getApprovedTransactionsByUserId'");
    }

}
