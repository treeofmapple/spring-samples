package com.tom.benchmark.order.dto;

import java.math.BigDecimal;

public record ProductRequest(
		
		String sku,
		String name,
		String description,
		BigDecimal price
		
		) {

}
