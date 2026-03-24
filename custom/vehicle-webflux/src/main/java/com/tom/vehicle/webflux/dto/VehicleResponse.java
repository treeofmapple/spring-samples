package com.tom.vehicle.webflux.dto;

public record VehicleResponse(
		
		Long id,
		String brand,
		String model,
		String color,
		String plate

) {

}