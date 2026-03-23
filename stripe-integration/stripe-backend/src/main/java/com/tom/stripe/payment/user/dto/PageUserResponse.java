package com.tom.stripe.payment.user.dto;

import java.util.List;

public record PageUserResponse(
		
		List<SimpleUserResponse> content,
		Integer page,
		Integer size,
		Integer totalPages
		
) {

}
