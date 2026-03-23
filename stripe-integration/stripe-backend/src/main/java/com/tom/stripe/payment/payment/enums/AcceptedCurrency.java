package com.tom.stripe.payment.payment.enums;

import java.util.Arrays;

import com.tom.stripe.payment.exception.server.InvalidEnumException;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AcceptedCurrency {

	USD("usd"), BRL("brl");
	
	private final String currencyString;

	public static AcceptedCurrency fromString(String text) {
        return Arrays.stream(AcceptedCurrency.values())
                .filter(c -> c.currencyString.equalsIgnoreCase(text))
                .findFirst()
                .orElseThrow(() -> new InvalidEnumException("Unsupported currency: " + text));
    }
	
	public static void isValid(AcceptedCurrency currency) {
		if (currency == null) {
			throw new InvalidEnumException("Currency cannot be null");
		}
		
		Boolean exists = Arrays.stream(AcceptedCurrency.values())
                .anyMatch(enumValue -> enumValue.equals(currency));

		if (!exists) {
			throw new InvalidEnumException("Unsupported currency: " + currency);
		}
    }
	
}
