package com.tom.stripe.payment.payment.enums;

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
	
}
