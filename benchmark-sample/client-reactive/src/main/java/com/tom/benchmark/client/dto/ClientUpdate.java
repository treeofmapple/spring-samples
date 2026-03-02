package com.tom.benchmark.client.dto;

import java.util.UUID;

public record ClientUpdate(
		
		UUID userId,
		String name,
		String cpf
		
) {

}
