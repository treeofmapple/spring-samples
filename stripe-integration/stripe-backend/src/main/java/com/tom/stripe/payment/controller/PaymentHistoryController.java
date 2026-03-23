package com.tom.stripe.payment.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.tom.stripe.payment.history.component.PaymentHistoryComponent;
import com.tom.stripe.payment.history.dto.PagePaymentHistoryResponse;
import com.tom.stripe.payment.history.dto.PaymentHistoryResponse;

import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequestMapping("/v1/payment/history")
@RequiredArgsConstructor
public class PaymentHistoryController {

	private final PaymentHistoryComponent service;

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public PagePaymentHistoryResponse searchHistoryByPage(
			@RequestParam(defaultValue = "0", required = false) int page) {
		return service.findByParams(page);
	}

	@GetMapping(value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	public PaymentHistoryResponse searchHistoryById(@PathVariable(value = "id") UUID query) {
		return service.findById(query);
	}

}
