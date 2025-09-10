package com.franndelgado.payment_transactions_api.service;

import java.util.List;

import com.franndelgado.payment_transactions_api.dto.TransactionDTO;
import com.franndelgado.payment_transactions_api.enums.TransactionStatus;

public interface TransactionService {
    TransactionDTO createTransaction(TransactionDTO transactionDTO);
    TransactionStatus getTransactionStatus(String transaction_id);
    List<TransactionDTO> getApprovedTransactionsByUserId(String user_id);
}
