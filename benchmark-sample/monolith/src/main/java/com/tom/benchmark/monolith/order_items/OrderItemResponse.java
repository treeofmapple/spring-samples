package com.tom.benchmark.monolith.order_items;

import java.math.BigDecimal;

public record OrderItemResponse(
		
		Long id,
		String productName,
		int quantity,
		BigDecimal priceAtPurchase,
		BigDecimal itemTotal
		
		) {

}
