package com.tom.stripe.payment.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.tom.stripe.payment.dto.RefundRequest;
import com.tom.stripe.payment.payment.dto.PagePaymentResponse;
import com.tom.stripe.payment.payment.dto.PaymentRequest;
import com.tom.stripe.payment.payment.dto.PaymentResponse;
import com.tom.stripe.payment.payment.dto.PaymentUpdate;
import com.tom.stripe.payment.payment.repository.filtering.PaymentSortOption;
import com.tom.stripe.payment.payment.service.PaymentService;

import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequestMapping("/v1/payment")
@RequiredArgsConstructor
public class PaymentController {

	private final PaymentService service;

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public PagePaymentResponse searchPaymentByParam(@RequestParam(defaultValue = "0", required = false) int page,
			@RequestParam(required = false) PaymentSortOption sortParam) {
		return service.searchPaymentByParams(page, sortParam);
	}

	@GetMapping(value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	public PaymentResponse searchPaymentById(@PathVariable(value = "id") UUID query) {
		return service.searchPaymentById(query);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public PaymentResponse createPayment(@RequestBody PaymentRequest request) {
		return service.createPayment(request);
	}

	@PostMapping(value = "/refund")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public PaymentResponse processUserRefund(@RequestBody RefundRequest request) {
		return service.requireRefund(request);
	}
	
	@PutMapping
	@ResponseStatus(HttpStatus.ACCEPTED)
	public PaymentResponse updatePayment(@RequestBody PaymentUpdate request) {
		return service.updatePayment(request);
	}
	
	@DeleteMapping(value = "/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deletePayment(UUID query) {
		service.deletePayment(query);
	}

}
