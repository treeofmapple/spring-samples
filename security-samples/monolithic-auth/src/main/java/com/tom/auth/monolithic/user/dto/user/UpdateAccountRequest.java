package com.tom.auth.monolithic.user.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public record UpdateAccountRequest(
		
	    @Size(min = 4, max = 40, message = "Username must be at least 4 characters and limited to 40")
	    String username,

	    @Email(message = "Email must be valid")
	    String email,

	    @Min(value = 13, message = "Age must be at least 13")
	    Integer age

		) {

}
