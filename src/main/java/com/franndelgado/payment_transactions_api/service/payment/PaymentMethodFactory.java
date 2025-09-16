package com.franndelgado.payment_transactions_api.service.payment;

import org.springframework.stereotype.Component;

@Component
public class PaymentMethodFactory {

    public PaymentMethod createPaymentMethod(String paymentType) {
        switch (paymentType.toUpperCase()) {
            case "BANK_TRANSFER":
                return new BankTransferPayment();
            default:
                throw new IllegalArgumentException("Unsupported payment type: " + paymentType);
        }
    }
}
