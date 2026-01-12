package com.tom.vehicle.normal.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record VehicleResponse(
		
		Long id,
		String brand,
		String model,
		String color,
		String plate

) {

}