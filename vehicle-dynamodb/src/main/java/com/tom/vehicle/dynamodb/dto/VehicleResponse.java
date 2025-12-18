package com.tom.vehicle.dynamodb.dto;

public record VehicleResponse(
		
		Long id,
		String brand,
		String model,
		String color,
		String plate

) {

}