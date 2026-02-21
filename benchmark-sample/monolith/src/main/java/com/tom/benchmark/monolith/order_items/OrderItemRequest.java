package com.tom.benchmark.monolith.order_items;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record OrderItemRequest(
		
	    @NotNull(message = "Product sku cannot be null")
	    String productSku,
		
	    @Min(value = 1, message = "Quantity must be at least 1")
		int quantity
		
		) {

}
