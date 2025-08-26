package com.tom.auth.monolithic.user.dto.admin;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"loginTime", "ipAddress"})
public record LoginHistoryResponse(
		
		ZonedDateTime loginTime,
		String ipAddress
		
		) {

}
