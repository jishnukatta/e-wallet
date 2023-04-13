package com.ewallet.transactionservice;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionCreateRequest {

	private Integer senderUserId;
	
	private Integer receiverUserId;
	
	private Double amount;
	
	private String purpose;
	
	public Transaction to()
	{
		return Transaction.builder().senderUserId(this.senderUserId).receiverUserId(this.receiverUserId)
				.amount(this.amount).purpose(this.purpose).build();
	}
}
