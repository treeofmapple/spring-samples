package com.tom.benchmark.monolith.order;

import java.math.BigDecimal;
import java.util.List;

import com.tom.benchmark.monolith.order_items.OrderItemResponse;

public record OrderResponse(
		
	    Long id,
	    String clientName,
	    List<OrderItemResponse> items,
	    BigDecimal totalPrice
		
		) {

}
