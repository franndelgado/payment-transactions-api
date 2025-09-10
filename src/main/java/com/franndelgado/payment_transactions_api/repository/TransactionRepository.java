package com.franndelgado.payment_transactions_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.franndelgado.payment_transactions_api.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, String> {

}
