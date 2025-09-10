package com.franndelgado.payment_transactions_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.franndelgado.payment_transactions_api.entity.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {

}
