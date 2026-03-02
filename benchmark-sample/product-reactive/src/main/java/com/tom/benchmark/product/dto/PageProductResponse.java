package com.tom.benchmark.product.dto;

import java.util.List;

public record PageProductResponse(
		
		List<ProductResponse> content,
		Integer page,
		Integer size,
		Integer totalpages,
		Long totalElements
		
) {

}
