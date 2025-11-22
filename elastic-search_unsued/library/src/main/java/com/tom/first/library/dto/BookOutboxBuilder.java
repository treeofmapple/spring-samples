package com.tom.first.library.dto;

import java.time.ZonedDateTime;

import com.tom.first.library.model.enums.EventType;

public record BookOutboxBuilder(
		
		EventType eventType,
		String payload,
		ZonedDateTime createdAt
		
		) {

}
