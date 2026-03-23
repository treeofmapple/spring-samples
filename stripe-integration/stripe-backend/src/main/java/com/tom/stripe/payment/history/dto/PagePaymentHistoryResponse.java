package com.tom.stripe.payment.history.dto;

import java.util.List;

public record PagePaymentHistoryResponse(
		
		List<PaymentHistoryResponse> content,
		Integer page,
		Integer size,
		Integer totalPages
		
) {

}
