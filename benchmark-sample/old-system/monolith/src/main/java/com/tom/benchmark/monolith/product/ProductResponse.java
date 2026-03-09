package com.tom.benchmark.monolith.product;

import java.math.BigDecimal;

public record ProductResponse(
		
		Long id,
		String sku,
		String name,
		String description,
		BigDecimal price
		
) {

}
