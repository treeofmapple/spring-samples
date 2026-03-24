package com.tom.first.vehicle.dto;

import java.time.ZonedDateTime;

import com.tom.first.vehicle.model.enums.EventType;

public record VehicleOutboxBuild(
		
		EventType eventType,
		String payload,
		ZonedDateTime createdAt
		
		) {

}
