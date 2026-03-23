package com.tom.stripe.payment.user.dto;

import java.util.UUID;

public record PostalInfoRequest(
		
		UUID userId,
		String taxId,
		String postalCode,
		String countryCode
		
) {

}
