package com.tom.stripe.payment.logic.payment;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/v1/webhook/stripe")
public class StripePaymentWebhook {

	@Value("${stripe.webhook.secret}")
	private String endpointSecret;

	@PostMapping
	public ResponseEntity<String> handleWebhook(@RequestBody String payload,
			@RequestHeader("Stripe-Signature") String sigHeader) {
		Event event;
		try {
			event = Webhook.constructEvent(payload, sigHeader, sigHeader);
		} catch (SignatureVerificationException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Signature");
		}

		EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
		
		switch (event.getType()) {

		case "payment_intent.succeeded":
			dataObjectDeserializer.getObject().ifPresent(obj -> {
				PaymentIntent intent = (PaymentIntent) obj;
				
			});
			break;
		case "payment_intent.payment_failed":
			dataObjectDeserializer.getObject().ifPresent(obj -> {
				
			});
			break;
		case "charge.refunded":
			dataObjectDeserializer.getObject().ifPresent(obj -> {
			
			});
			break;
		case "payment_intent.processing":
			dataObjectDeserializer.getObject().ifPresent(obj -> {
			
			});
			break;
		case "payment_intent.canceled":
			dataObjectDeserializer.getObject().ifPresent(obj -> {
			
			});
			break;
		case "payment_intent.requires_action":
			dataObjectDeserializer.getObject().ifPresent(obj -> {
			
			});
			break;
		default:
			log.info("Received unhandled event type: {}", event.getType());
		}

		return ResponseEntity.ok().build();
	}
}
