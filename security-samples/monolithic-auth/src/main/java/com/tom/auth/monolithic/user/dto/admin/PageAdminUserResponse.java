package com.tom.auth.monolithic.user.dto.admin;

import java.util.List;

public record PageAdminUserResponse(
		
		List<UserAdminResponse> content,
		int page,
		int size,
		long totalPages,
		long totalElements
		
		) {

}
