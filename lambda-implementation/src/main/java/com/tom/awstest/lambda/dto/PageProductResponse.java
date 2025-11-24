package com.tom.awstest.lambda.dto;

import java.util.List;

public record PageProductResponse(
		
		List<ProductResponse> content,
		int page,
		int size,
		long totalPages,
		long totalElements
		
		) {

}
