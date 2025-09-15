package com.franndelgado.payment_transactions_api.service;

import java.util.List;

import com.franndelgado.payment_transactions_api.dto.TransactionRequestDTO;
import com.franndelgado.payment_transactions_api.dto.TransactionResponseDTO;
import com.franndelgado.payment_transactions_api.enums.TransactionStatus;

public interface TransactionService {
    TransactionResponseDTO createTransaction(TransactionRequestDTO transactionDTO);
    TransactionStatus getTransactionStatus(String transaction_id);
    List<TransactionResponseDTO> getApprovedTransactionsByUserId(String user_id);
}
