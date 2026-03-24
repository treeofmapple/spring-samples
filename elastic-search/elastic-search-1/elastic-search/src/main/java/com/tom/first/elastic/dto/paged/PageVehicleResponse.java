package com.tom.first.elastic.dto.paged;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tom.first.elastic.dto.VehicleResponse;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public record PageVehicleResponse(
		
		List<VehicleResponse> content,
		int page,
		int size,
		long totalElements
		
		) {

}
