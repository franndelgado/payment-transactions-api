package com.franndelgado.payment_transactions_api.service.payment;

import com.franndelgado.payment_transactions_api.entity.Transaction;

public interface PaymentMethod {
    boolean processPayment(Transaction transaction);
}
