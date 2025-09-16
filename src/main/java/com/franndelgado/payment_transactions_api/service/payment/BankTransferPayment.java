package com.franndelgado.payment_transactions_api.service.payment;

import org.springframework.stereotype.Service;

import com.franndelgado.payment_transactions_api.entity.Transaction;

@Service
public class BankTransferPayment implements PaymentMethod {

    @Override
    public boolean processPayment(Transaction transaction) {
        return processeBankTransfer(transaction);
    }
    
    private boolean processeBankTransfer(Transaction transaction) {
        return true;
    }

}
