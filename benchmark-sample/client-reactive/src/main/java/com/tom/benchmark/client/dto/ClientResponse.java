package com.tom.benchmark.client.dto;

import java.time.ZonedDateTime;
import java.util.UUID;

public record ClientResponse(
		
		UUID id,
		String name,
		String cpf,
		ZonedDateTime createdAt
		
) {

}
