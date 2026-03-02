package com.tom.benchmark.client.dto;

import java.util.List;

public record PageClientResponse(
		
		List<ClientResponse> content,
		Integer page,
		Integer size,
		Integer totalpages,
		Long totalElements
		
) {

}
