package com.tom.auth.monolithic.user.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
		
	    @NotBlank(message = "Username must not be blank")
	    @Size(min = 4, max = 40, message = "Username must be at least 4 characters and limited to 40")
		String username,
		
		@NotBlank(message = "Email must not be blank")
		@Email(message = "Email must be valid")
		String email,
		
	    @NotNull(message = "Age must not be blank")
		@Min(value = 13, message = "You must be at least 13 years old")
		Integer age,
		
	    @NotBlank(message = "Password must not be blank")
	    @Size(min = 8, message = "Password must be at least 8 characters")
		String password,
		
	    @NotBlank(message = "Confirm password must not be blank")		
		String confirmPassword
		
		) {

}
