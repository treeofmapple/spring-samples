package com.tom.auth.monolithic.user.dto.admin;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonAlias;

public record IdentifierRequest(
		
		UUID userId,
		
		@JsonAlias({"username", "email"})
		String identifier
		
		) {

}
