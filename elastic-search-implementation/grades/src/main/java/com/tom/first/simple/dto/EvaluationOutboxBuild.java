package com.tom.first.simple.dto;

import java.time.ZonedDateTime;

import com.tom.first.simple.model.enums.EventType;

public record EvaluationOutboxBuild(
		
		EventType eventType,
		String payload,
		ZonedDateTime createdAt
		
		) {

}
