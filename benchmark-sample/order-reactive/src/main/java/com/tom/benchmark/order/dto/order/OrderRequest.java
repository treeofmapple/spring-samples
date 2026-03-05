package com.tom.benchmark.order.dto.order;

import com.fasterxml.jackson.annotation.JsonAlias;

public record OrderRequest(
		
		@JsonAlias( {
				"clientCpf", "cpf" })
		String clientCpf
	
) {

}
