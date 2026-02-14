package com.tom.reactive.first.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponse(

		UUID id,
		String nickname,
		String email,
		String password,
		String phoneNumber,
		String role,
		String bio,
		Boolean active,
		LocalDateTime createdAt
		
) {

}
