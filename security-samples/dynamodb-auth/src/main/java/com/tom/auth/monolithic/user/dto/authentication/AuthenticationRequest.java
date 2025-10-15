package com.tom.auth.monolithic.user.dto.authentication;

import com.fasterxml.jackson.annotation.JsonAlias;

import jakarta.validation.constraints.NotBlank;

public record AuthenticationRequest(
		
		@JsonAlias({"username", "email"}) 
		@NotBlank(message = "Username or email must be inserted")
		String userinfo,
		
		@NotBlank(message = "Password must not be blank")
		String password
		
		) {

}
