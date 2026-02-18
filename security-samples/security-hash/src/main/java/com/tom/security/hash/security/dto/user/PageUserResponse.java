package com.tom.security.hash.security.dto.user;

import java.util.List;

public record PageUserResponse(
		
		List<UserResponse> content,
		Integer page,
		Integer size,
		Integer totalPages
		
) {

}
