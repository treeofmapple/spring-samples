package com.tom.reactive.first.dto;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonAlias;

public record UserUpdateRequest(
		
		@JsonAlias( {
				"userId",
				"id" })
		UUID userId,
		
		String nickname,
		
		String email,
		
		String password,
		
		String phoneNumber,
		
		String role,
		
		String bio
		
) {

}
