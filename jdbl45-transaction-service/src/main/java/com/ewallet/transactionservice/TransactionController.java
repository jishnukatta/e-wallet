package com.ewallet.transactionservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionController {

	@Autowired
	TransactionService txnService;
	
	
	@PostMapping("/transact")
	public String createTxn(@RequestBody TransactionCreateRequest txnCreateReq)
	{
		String txnId=txnService.createTxn(txnCreateReq.to());
	
		
		return "Your transaction has been initiated, here is the txnId" + txnId;
	}
	
}

