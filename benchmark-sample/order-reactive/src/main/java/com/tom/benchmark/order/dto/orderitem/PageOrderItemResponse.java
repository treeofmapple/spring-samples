package com.tom.benchmark.order.dto.orderitem;

import java.util.List;

public record PageOrderItemResponse(
		
		List<OrderItemResponse> content,
		Integer page,
		Integer size,
		Integer totalPages,
		Long totalElements
		
) {

}
