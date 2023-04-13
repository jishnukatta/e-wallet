package com.ewallet.walletservice;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class WalletService {

	private static final String USER_CREATE_TOPIC = "user_create";
	private static final String TXN_CREATE_TOPIC="txn_create";
	private static final String WALLET_UPDATE_TOPIC="wallet_update";
	
	private int initialAmount=100;
	
	@Autowired
	WalletRepository walletRepo;
	
	@Autowired
	KafkaTemplate<String,String> kft;
	
	@KafkaListener(topics= {USER_CREATE_TOPIC},groupId = "foo")
	public void walletCreate(String message) throws Exception
	{
		JSONObject jsonObject=(JSONObject) new JSONParser().parse(message);
		
		if(!jsonObject.containsKey("userId"))
		{
			throw new Exception("user id is not present in the user event");
		}
		int userId=((Long)jsonObject.get("userId")).intValue();
		
		Wallet wallet=Wallet.builder().balance(initialAmount).userId(userId).build();
		walletRepo.save(wallet);
		
	}
	
	
	
	
	@KafkaListener(topics= {TXN_CREATE_TOPIC},groupId = "foo")
	public void walletUpdate(String message) throws Exception
	{
		JSONObject jsonObj=(JSONObject) new JSONParser().parse(message);
		
		if(!jsonObj.containsKey("senderId") 
				|| !jsonObj.containsKey("receiverId") 
				|| !jsonObj.containsKey("amount")
				|| !jsonObj.containsKey("txnId"))
			
		{
			throw new Exception("some of the details not present in the create txn event");
		}
		
		Integer senderId =((Long) jsonObj.get("senderId")).intValue();
		Integer receiverId =((Long) jsonObj.get("receiverId")).intValue();
		Double amount =(Double) jsonObj.get("amount");
		String txnId = (String)jsonObj.get("txnId");
		
		Wallet senderWallet = walletRepo.findByUserId(senderId);
		
		JSONObject walletUpdateEvent = new JSONObject();
		
		walletUpdateEvent.put("txnId", txnId);
		
		if(senderWallet.getBalance() < amount)
		{
			walletUpdateEvent.put("status", "FAILED");
		}
		else
		{
			walletRepo.updateWallet(receiverId,amount);
			walletRepo.updateWallet(senderId, -1*amount);
			walletUpdateEvent.put("status", "SUCCESSFULL");
			
		}
		kft.send(WALLET_UPDATE_TOPIC,walletUpdateEvent.toJSONString());
	}
}
