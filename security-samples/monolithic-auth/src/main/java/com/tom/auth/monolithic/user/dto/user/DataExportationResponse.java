package com.tom.auth.monolithic.user.dto.user;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.tom.auth.monolithic.user.dto.admin.LoginHistoryResponse;

@JsonPropertyOrder({"userId", "username", "email", "age", "role", "accountEnabled", "loginHistory"})
public record DataExportationResponse(
		
	    UUID userId,
	    String username,
	    String email,
	    Integer age,
	    String role,
	    boolean accountEnabled,
	    List<LoginHistoryResponse> loginHistory
		
		) {

	
}
