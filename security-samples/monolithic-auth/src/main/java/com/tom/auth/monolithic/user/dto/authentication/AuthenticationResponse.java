package com.tom.auth.monolithic.user.dto.authentication;

public record AuthenticationResponse(
		
		String accessToken,

		String refreshToken
		
		
		) {

}
