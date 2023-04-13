package com.ewallet.userservice;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateRequest {

	private String name;
	private String email;
	private String contact;
	
	public User to()
	{
		return User.builder().name(this.name)
				.email(this.email).contact(this.contact).build();
	}
}
