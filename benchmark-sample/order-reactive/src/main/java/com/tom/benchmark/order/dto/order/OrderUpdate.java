package com.tom.benchmark.order.dto.order;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonAlias;

public record OrderUpdate(

		UUID orderId,
		@JsonAlias( {
			"clientCpf", "cpf" })
		String clientCpf
		
) {

}
