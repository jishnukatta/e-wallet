package com.ewallet.userservice;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserService {

	@Autowired
	UserRepository userRepo;
	
	@Autowired
	KafkaTemplate<String,String> kft;
	
	private static final String USER_CREATE_TOPIC = "user_create";
	
	public User create(User user) {
		
		user=userRepo.save(user);
		//publishing data to kafka to send to wallet service
		
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("userId", user.getId());
		jsonObject.put("userEmail", user.getEmail());
		jsonObject.put("userContact", user.getContact());
		
		kft.send(USER_CREATE_TOPIC, jsonObject.toJSONString());

		return user;
	}

	public User get(int id) {
		return userRepo.findById(id).orElse(null);
	}

	
	
}
