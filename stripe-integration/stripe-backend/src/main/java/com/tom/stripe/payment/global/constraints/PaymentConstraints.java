package com.tom.stripe.payment.global.constraints;

import org.springframework.beans.factory.annotation.Value;

public class PaymentConstraints {

	@Value("${stripe.payment.timeout}")
	public long PAYMENT_TIMEOUT_ISSUE = 300; 
	
	protected PaymentConstraints() {
	}
	
}
