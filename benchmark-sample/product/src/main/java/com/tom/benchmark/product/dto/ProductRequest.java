package com.tom.benchmark.product.dto;

import java.math.BigDecimal;

public record ProductRequest(
		
		String sku,
		String name,
		String description,
		BigDecimal price
		
		) {

}
