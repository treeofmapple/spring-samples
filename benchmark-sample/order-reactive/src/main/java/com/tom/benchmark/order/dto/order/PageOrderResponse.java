package com.tom.benchmark.order.dto.order;

import java.util.List;

public record PageOrderResponse(
		
		List<OrderResponse> content,
		Integer page,
		Integer size,
		Integer totalpages,
		Long totalElements
		
) {

}
