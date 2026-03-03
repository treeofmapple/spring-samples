package com.tom.benchmark.order.dto.orderitem;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItemResponse(
		
		UUID id,
		String productName,
		Integer quantity,
		BigDecimal priceAtPurchase,
		BigDecimal itemTotal
		
) {

}
