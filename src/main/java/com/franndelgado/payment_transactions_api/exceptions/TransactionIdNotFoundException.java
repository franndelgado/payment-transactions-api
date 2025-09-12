package com.franndelgado.payment_transactions_api.exceptions;

public class TransactionIdNotFoundException extends RuntimeException {

    public TransactionIdNotFoundException(String transactionId){
        super(String.format("The transaction identifier '%s' does not exist.", transactionId));
    }
}
