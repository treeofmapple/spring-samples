package com.tom.stripe.payment.payment.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AcceptedCurrency {

	USD("usd"), BRL("brl");
	
	private final String currencyString;
	
}
