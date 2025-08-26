package com.tom.sample.auth.dto;

public record AuthenticationResponse(

		String accessToken,

		String refreshToken
		
) {
}
