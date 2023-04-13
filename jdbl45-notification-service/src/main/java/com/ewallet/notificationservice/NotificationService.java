package com.ewallet.notificationservice;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

	
	@Autowired
	SimpleMailMessage simpleMailMessage;
	
	@Autowired
	JavaMailSender javaMailSender;
	
	private static final String TXN_COMPLETE_TOPIC="txn_complete";

	
	@KafkaListener(topics= {TXN_COMPLETE_TOPIC},groupId="foo")
	public void sendNotification(String message) throws Exception
	{
		JSONObject jsonObject=(JSONObject) new JSONParser().parse(message);
		
		
		String txnId=(String)jsonObject.get("txnId");
		String status=(String)jsonObject.get("status");
		String senderEmail=(String)jsonObject.get("senderEmail");
		String receiverEmail=(String)jsonObject.get("receiverEmail");
		Double amount=(Double)jsonObject.get("amount");
		
		
		// Sending mail to sender
        simpleMailMessage.setText("Hi, your txn with id " + txnId + " got " + status);
        simpleMailMessage.setTo(senderEmail);
        simpleMailMessage.setSubject("Payment Notification");
        simpleMailMessage.setFrom("jishnukatta123@gmail.com");

        javaMailSender.send(simpleMailMessage);
        
        
        if("SUCCESSFULL".equals(status)){
            // Sending mail to receiver
            simpleMailMessage.setText("Hi, you got amount " + amount + " from user " + senderEmail + " in your e-wallet");
            simpleMailMessage.setTo(receiverEmail);
            javaMailSender.send(simpleMailMessage);
        }
		
	}
}
