package com.tom.stripe.payment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.tom.stripe.payment.config.CustomBanner;

@SpringBootApplication
public class StripeIntegrationApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(StripeIntegrationApplication.class);
		app.setBanner(new CustomBanner());
		app.run(args);
	}

}
