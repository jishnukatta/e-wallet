package com.ewallet.transactionservice;

import org.apache.kafka.common.Uuid;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TransactionService {
	
	private static final String TXN_CREATE_TOPIC="txn_create";
	private static final String WALLET_UPDATE_TOPIC="wallet_update";
	private static final String TXN_COMPLETE_TOPIC="txn_complete";


	@Autowired
	TransactionRepository txnRepo;
	
	@Autowired
	KafkaTemplate<String,String> kft;
	
	@Autowired
	RestTemplate restTemplate;
	
	public String createTxn(Transaction transaction) {
		
		transaction.setTxnId(Uuid.randomUuid().toString());
		
		transaction.setStatus(TransactionStatus.PENDING);
		
		txnRepo.save(transaction);
		
		JSONObject jsonobj=new JSONObject();
		
		jsonobj.put("senderId", transaction.getSenderUserId());
		jsonobj.put("receiverId", transaction.getReceiverUserId());
		jsonobj.put("amount", transaction.getAmount());
		jsonobj.put("txnId", transaction.getTxnId());
		
		kft.send(TXN_CREATE_TOPIC, jsonobj.toJSONString());
		
		
		return transaction.getTxnId();
	}
	
	
	@KafkaListener(topics= {WALLET_UPDATE_TOPIC},groupId = "foo")
	public void updateTxn(String message) throws ParseException
	{
		
              JSONObject jsonObj=(JSONObject) new JSONParser().parse(message);
		
              String txnId=(String)jsonObj.get("txnId");
              String status=(String)jsonObj.get("status");
              
              TransactionStatus transactionStatus;
              
              if(status.equals("FAILED"))
              {
            	  transactionStatus=TransactionStatus.FAILED;
              }
              else
              {
            	  transactionStatus=TransactionStatus.SUCCESSFULL;

              }
              
              Transaction txn=txnRepo.findByTxnId(txnId);
				
              txn.setStatus(transactionStatus);
              
              txnRepo.save(txn);
              
              
              
              JSONObject senderUser = restTemplate.getForObject("http://localhost:9003/user?id="+txn.getSenderUserId(), JSONObject.class);
            		  
              String senderEmail=(String)senderUser.get("email");
              
              JSONObject receiverUser=restTemplate.getForObject("http://localhost:9003/user?id="+txn.getReceiverUserId(), JSONObject.class);
              
              String receiverEmail=(String)receiverUser.get("email");
              
              
              JSONObject txnCompleteEvent=new JSONObject();
              
              txnCompleteEvent.put("txnId",txn.getTxnId());
              txnCompleteEvent.put("senderEmail",senderEmail);
              txnCompleteEvent.put("receiverEmail",receiverEmail);
              txnCompleteEvent.put("status",txn.getStatus().name());
              txnCompleteEvent.put("amount",txn.getAmount());

              
              
              kft.send(TXN_COMPLETE_TOPIC, txnCompleteEvent.toJSONString());
              
	}

}
