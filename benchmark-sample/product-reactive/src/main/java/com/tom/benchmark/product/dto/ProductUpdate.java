package com.tom.benchmark.product.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductUpdate(
		
		UUID productId,
		String sku,
		String name,
		String description,
		BigDecimal price
		
) {

}
