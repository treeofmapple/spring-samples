package com.tom.benchmark.order.dto;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record OrderRequest(
		
	    @NotNull(message = "Client cpf cannot be null")
	    String clientCpf,

	    @NotEmpty(message = "Order must contain at least one item")
	    List<OrderItemRequest> items
		
		) {

}
