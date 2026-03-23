package com.tom.stripe.payment.dto;

import java.util.UUID;

public record RefundRequest(
		
		UUID userId,
		UUID paymentIntent
		
) {

}
