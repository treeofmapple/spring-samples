package com.tom.stripe.payment.payment.dto;

import java.util.List;

public record PagePaymentResponse(
		
		List<PaymentResponse> content,
		Integer page,
		Integer size,
		Integer totalPages
		
) {

}
