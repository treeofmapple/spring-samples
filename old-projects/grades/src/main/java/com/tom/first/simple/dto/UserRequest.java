package com.tom.first.simple.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRequest(

		@NotBlank(message = "Username cannot be blank")
		String name,
		
		@NotBlank(message = "Email cannot be blank")
		@Email(message = "Email is required to be valid")
		String email
		
		) {

}
