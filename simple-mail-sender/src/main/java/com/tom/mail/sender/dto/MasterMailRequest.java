package com.tom.mail.sender.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record MasterMailRequest(
		
		@NotEmpty(message = "Master mail cannot be empty")
		@Email(message = "Mail needs to be validated.")
		String mail
		
) {

}
