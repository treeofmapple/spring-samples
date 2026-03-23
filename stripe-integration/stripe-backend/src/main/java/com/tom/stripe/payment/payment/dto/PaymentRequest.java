package com.tom.stripe.payment.payment.dto;

import java.math.BigDecimal;
import java.util.UUID;

import com.tom.stripe.payment.payment.enums.AcceptedCurrency;
import com.tom.stripe.payment.payment.enums.PaymentMethods;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public record PaymentRequest(
		
		UUID userId,

		@DecimalMin(value = "0.00")
		@NotNull(message = "Amount is required")
		BigDecimal amount,
		
		AcceptedCurrency currency,
		
		PaymentMethods paymentMethod
		
) {

}
