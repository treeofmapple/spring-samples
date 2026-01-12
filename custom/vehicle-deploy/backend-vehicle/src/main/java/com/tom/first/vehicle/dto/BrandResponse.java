package com.tom.first.vehicle.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record BrandResponse(
		
		Long id,
		String name
		
		) {

}
