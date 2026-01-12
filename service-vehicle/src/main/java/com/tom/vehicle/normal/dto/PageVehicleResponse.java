package com.tom.vehicle.normal.dto;

import java.util.List;

public record PageVehicleResponse(
		
		List<VehicleResponse> content,
		int page,
		int size,
		long totalPages
		
		) {

}
