package com.tom.stripe.payment.user.dto;

public record PostalInfoRequest(
		
		String taxId,
		String postalCode,
		String countryCode
		
) {

}
