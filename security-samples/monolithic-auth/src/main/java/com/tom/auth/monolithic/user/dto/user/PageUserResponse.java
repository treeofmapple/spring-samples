package com.tom.auth.monolithic.user.dto.user;

import java.util.List;

public record PageUserResponse(
		
		List<UserResponse> content,
		int page,
		int size,
		long totalPages,
		long totalElements
		
		
		) {

}
