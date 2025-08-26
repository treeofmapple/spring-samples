package com.tom.auth.monolithic.user.dto.admin;

import java.util.List;

public record PageLoginHistoryResponse(
		
		List<LoginHistoryResponse> content,
		int page,
		int size,
		long totalPages,
		long totalElements
		
		) {

}
