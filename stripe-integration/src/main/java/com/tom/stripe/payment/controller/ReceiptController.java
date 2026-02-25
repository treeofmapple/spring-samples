package com.tom.stripe.payment.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequestMapping("/v1/receipt")
@RequiredArgsConstructor
public class ReceiptController {

	// get a receipt get it number and its url
	
}
