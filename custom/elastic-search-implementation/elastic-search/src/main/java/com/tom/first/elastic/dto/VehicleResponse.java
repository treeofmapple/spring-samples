package com.tom.first.elastic.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tom.first.elastic.models.vehicle.Type;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public record VehicleResponse(
		
		String brand,
		String model,
		String color,
		String plate,
		Type type

) {

}