package com.tom.benchmark.monolith.product;

import java.math.BigDecimal;

public record ProductRequest(
		
		String sku,
		String name,
		String description,
		BigDecimal price
		
		) {

}
