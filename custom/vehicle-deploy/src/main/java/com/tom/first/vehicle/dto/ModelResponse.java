package com.tom.first.vehicle.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ModelResponse(
		
		long id,
		String name,
		String brandName
		
		) {

}
