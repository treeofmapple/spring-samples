package com.tom.first.vehicle.dto;

import java.util.List;

public record PageVehicleResponse(
		
		List<VehicleResponse> content,
		int page,
		int size,
		long totalPages,
		long totalElements

		) {

}
