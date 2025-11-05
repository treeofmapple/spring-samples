package com.tom.first.library.dto;

import jakarta.validation.constraints.NotBlank;

public record ItemRequest(
		
		@NotBlank(message = "Book name can't be empty")
		String bookName,
		
		@NotBlank(message = "Username name can't be empty")
		String username
		
) {
	
}
