package com.tom.front.full.dto;

import java.util.List;

public record PageEmployeeResponse(
		
		List<EmployeeResponse> content,
		int page,
		int size,
		long totalPages,
		long totalElements
		
		) {

}
