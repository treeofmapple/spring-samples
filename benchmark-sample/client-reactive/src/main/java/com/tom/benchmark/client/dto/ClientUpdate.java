package com.tom.benchmark.client.dto;

import java.util.UUID;

public record ClientUpdate(
		
		UUID clientId,
		String name,
		String cpf
		
) {

}
