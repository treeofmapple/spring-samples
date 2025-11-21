package com.tom.first.elastic.dto;

import com.tom.first.elastic.models.vehicle.Type;

public record VehicleResponse(
		
		Long id,
		String brand,
		String model,
		String color,
		String plate,
		Type type

) {

}