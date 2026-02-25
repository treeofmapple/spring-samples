package com.tom.stripe.payment.payment.enums;

import java.util.Arrays;

import com.tom.stripe.payment.exception.server.InvalidEnumException;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentMethods {

	CREDIT_CARD("card"), 
    
    PAYPAL("paypal"),
    
    PIX("pix"),
    
    BOLETO("boleto");
	
	private final String paymentMethodString;

	public static PaymentMethods fromString(String text) {
        return Arrays.stream(PaymentMethods.values())
                .filter(c -> c.paymentMethodString.equalsIgnoreCase(text))
                .findFirst()
                .orElseThrow(() -> new InvalidEnumException("Unsupported payment method: " + text));
    }

	public static void isValid(PaymentMethods currency) {
		if (currency == null) {
			throw new InvalidEnumException("PaymentMethod cannot be null");
		}
		
		Boolean exists = Arrays.stream(PaymentMethods.values())
                .anyMatch(enumValue -> enumValue.equals(currency));

		if (!exists) {
			throw new InvalidEnumException("Unsupported payment method: " + currency);
		}
    }
	
}
