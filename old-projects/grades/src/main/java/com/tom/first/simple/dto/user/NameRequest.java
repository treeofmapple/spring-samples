package com.tom.first.simple.dto.user;

import jakarta.validation.constraints.NotBlank;

public record NameRequest(
		
		@NotBlank(message = "Username cannot be blank")
		String name
		
		) {

}
