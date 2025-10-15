package com.tom.auth.monolithic.user.dto.authentication;

import jakarta.validation.constraints.NotBlank;

public record PasswordAuthenticationRequest(
		
		@NotBlank(message = "Password must not be blank")
		String password,

		@NotBlank(message = "Confirm Password must not be blank")
		String confirmPassword
		
		
		) {

}
