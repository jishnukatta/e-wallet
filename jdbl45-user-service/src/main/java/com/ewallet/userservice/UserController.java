package com.ewallet.userservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

	
	@Autowired
	UserService userService;
	
	
	@PostMapping("/user")
	public User createUser(@RequestBody UserCreateRequest usercreatereq)
	{
		
		return userService.create(usercreatereq.to());
	}
	
	@GetMapping("/user")
	public User getUser(@RequestParam("id") int id)
	{
	
		return userService.get(id);
	}
	
}
