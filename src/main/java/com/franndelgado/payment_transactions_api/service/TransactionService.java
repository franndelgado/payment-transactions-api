package com.franndelgado.payment_transactions_api.service;

import org.springframework.data.domain.Page;

import com.franndelgado.payment_transactions_api.dto.TransactionRequestDTO;
import com.franndelgado.payment_transactions_api.dto.TransactionResponseDTO;
import com.franndelgado.payment_transactions_api.enums.TransactionStatus;

public interface TransactionService {
    TransactionResponseDTO createTransaction(TransactionRequestDTO transactionDTO);
    TransactionStatus getTransactionStatus(String transactionId);
    Page<TransactionResponseDTO> getApprovedTransactionsByUserId(String userId, int page, int size, String sort);
}
