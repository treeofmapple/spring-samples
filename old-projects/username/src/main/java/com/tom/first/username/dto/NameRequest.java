package com.tom.first.username.dto;

import jakarta.validation.constraints.NotBlank;

public record NameRequest(
		
		@NotBlank(message = "Username is required") 
		String name
		) {

}
