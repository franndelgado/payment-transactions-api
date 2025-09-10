package com.franndelgado.payment_transactions_api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.franndelgado.payment_transactions_api.dto.TransactionDTO;
import com.franndelgado.payment_transactions_api.enums.TransactionStatus;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Override
    public TransactionDTO createTransaction(TransactionDTO transactionDTO) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createTransaction'");
    }

    @Override
    public TransactionStatus getTransactionStatus(String transaction_id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTransactionStatus'");
    }

    @Override
    public List<TransactionDTO> getApprovedTransactionsByUserId(String user_id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getApprovedTransactionsByUserId'");
    }

}
