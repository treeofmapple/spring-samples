package com.tom.stripe.payment.payment.component;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.tom.stripe.payment.exception.sql.NotFoundException;
import com.tom.stripe.payment.payment.model.Payment;
import com.tom.stripe.payment.payment.repository.PaymentRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PaymentComponent {

	private final PaymentRepository repository;

	public Payment findById(UUID paymentId) {
		return repository.findById(paymentId).orElseThrow(
				() -> new NotFoundException(String.format("Payment with id %s, was not found.", paymentId)));
	}

}
