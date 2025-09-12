package com.franndelgado.payment_transactions_api.exceptions;

public class TransactionUserIdNotFoundException extends RuntimeException {

    public TransactionUserIdNotFoundException(String userId){
        super(String.format("The user identifier '%s' does not exist.", userId));
    }
}
