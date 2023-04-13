package com.ewallet.transactionservice;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
	
	Transaction findByTxnId(String txnId);

}
