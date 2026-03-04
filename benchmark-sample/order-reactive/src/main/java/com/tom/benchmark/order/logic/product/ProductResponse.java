package com.tom.benchmark.order.logic.product;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductResponse(
		
		UUID id,
		String sku,
		String name,
		String description,
		BigDecimal price
		
) {

}
