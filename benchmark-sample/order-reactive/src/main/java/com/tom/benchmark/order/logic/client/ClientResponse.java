package com.tom.benchmark.order.logic.client;

import java.time.ZonedDateTime;
import java.util.UUID;

public record ClientResponse(
		
		UUID id,
		String name,
		String cpf,
		ZonedDateTime createdAt
		
) {

}
