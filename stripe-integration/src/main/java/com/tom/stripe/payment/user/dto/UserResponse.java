package com.tom.stripe.payment.user.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record UserResponse(
		
		UUID id,
		String nickname,
		String email,
		
		/*
		 * These variables i would only pass if the system had an auth but not on a
		 * search By Id, i made only this for testing. But on a real system is a no no.
		 */
		
		String taxId,
		String postalCode,
		String countryCode,
		String stripeCustomerId,
		String defaultPaymentMethodId,
		BigDecimal balance
		
) {

}
