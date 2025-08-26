package com.tom.auth.monolithic.user.dto.admin;

import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record DeleteUsersRequest(
		
	    @NotEmpty
	    List<UUID> userIds,
		
		@NotBlank(message = "Password must not be blank")
		String password,

		@NotBlank(message = "Confirm Password must not be blank")
		String confirmPassword
		) {

}
