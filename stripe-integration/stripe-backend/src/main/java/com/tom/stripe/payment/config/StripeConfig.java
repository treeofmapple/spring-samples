package com.tom.stripe.payment.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.stripe.Stripe;
import com.tom.stripe.payment.exception.payment.PaymentException;

import jakarta.annotation.PostConstruct;

@Configuration
public class StripeConfig {

	@Value("${stripe.secret.key}")
	private String secretKey;

	@PostConstruct
	public void setup() {
		if (secretKey == null || secretKey.isBlank()) {
			throw new PaymentException(String.format("Stripe api key is missing: %s", secretKey));
		}
		Stripe.apiKey = secretKey;
	}

}
