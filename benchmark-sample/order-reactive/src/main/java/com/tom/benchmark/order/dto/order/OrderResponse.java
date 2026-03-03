package com.tom.benchmark.order.dto.order;

import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;

import com.tom.benchmark.order.model.OrderItem;

public record OrderResponse(
		
		UUID id,
		String clientName,
		Set<OrderItem> items,
		ZonedDateTime createdAt
		
) {

}
