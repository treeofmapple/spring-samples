package com.tom.first.vehicle.dto;

import com.tom.first.vehicle.model.enums.Type;

public record VehicleResponse(
		
		Long id,
		String brand,
		String model,
		String color,
		String plate,
		Type type

) {

}