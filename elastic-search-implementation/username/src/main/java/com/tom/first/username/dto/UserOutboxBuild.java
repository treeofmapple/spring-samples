package com.tom.first.username.dto;

import java.time.ZonedDateTime;

import com.tom.first.username.model.enums.EventType;

public record UserOutboxBuild(
		
		EventType eventType,
		String payload,
		ZonedDateTime createdAt
		
		) {

}
