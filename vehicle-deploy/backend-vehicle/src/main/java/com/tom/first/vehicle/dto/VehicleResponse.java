package com.tom.first.vehicle.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tom.first.vehicle.model.enums.Type;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public record VehicleResponse (

		Long id,
		String licensePlate,
		String brandName,
		String modelName, 
		String color, 
		Type type
) {

}