package com.tom.benchmark.order.dto;

import java.math.BigDecimal;

public record OrderItemResponse(
		
		Long id,
		String productName,
		int quantity,
		BigDecimal priceAtPurchase,
		BigDecimal itemTotal
		
		) {

}
