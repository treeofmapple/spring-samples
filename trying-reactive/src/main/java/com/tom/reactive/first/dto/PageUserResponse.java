package com.tom.reactive.first.dto;

import java.util.List;

public record PageUserResponse(
		
		List<UserResponse> content,
		Integer page,
		Integer size,
		Integer totalPages,
		Long totalElements
		
) {

}
