package com.tom.auth.monolithic.user.dto.user;

import jakarta.validation.constraints.NotBlank;

public record DeleteAccountRequest (
		
	    @NotBlank(message = "Password must not be blank")
		String password
		
		) {

}
