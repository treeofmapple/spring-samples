package com.tom.stripe.payment.user.dto;

import java.util.UUID;

public record UserUpdate(
		
		UUID userId,
		String nickname,
		String email,
		String taxId,
		String postalCode,
		String countryCode
		
) {

}
