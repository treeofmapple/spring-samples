package com.tom.benchmark.order.dto;

import java.math.BigDecimal;
import java.util.List;

public record OrderResponse(
		
	    Long id,
	    String clientName,
	    List<OrderItemResponse> items,
	    BigDecimal totalPrice
		
		) {

}
