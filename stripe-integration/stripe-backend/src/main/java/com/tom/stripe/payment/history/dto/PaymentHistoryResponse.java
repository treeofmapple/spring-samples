package com.tom.stripe.payment.history.dto;

import java.util.UUID;

import com.tom.stripe.payment.payment.enums.PaymentStatus;

public record PaymentHistoryResponse(
		
		UUID id,
		UUID paymentId,
		PaymentStatus status,
		String reason
		
) {

}
