package com.tom.sample.paypal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.servlet.view.RedirectView;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.tom.sample.paypal.service.PaypalService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PaypalController 
{

	private final PaypalService service;
	
	@GetMapping("/")
	public String home() 
	{
		return "index";
	}
	
	@PostMapping("/payment/create")
	public RedirectView createPayment(
			@RequestParam("method") String method,
			@RequestParam("amount") String amount,
			@RequestParam("currency") String currency,
			@RequestParam("description") String description,
			HttpServletRequest request
			) 
	{
	    String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
		
        String cancelUrl = baseUrl + "/payment/cancel";
        String successUrl = baseUrl + "/payment/success";
		
		Payment payment = service.createPayment(
				Double.valueOf(amount), 
				currency,
				method,
				"sale",
				description,
				cancelUrl,
				successUrl);
		
		for(Links links : payment.getLinks()) {
			if(links.getRel().equals(successUrl)) {
				return new RedirectView(links.getHref());
			}
		}
		return new RedirectView("/payment/error");	
	}
	
	
	@GetMapping("/payment/success")
	public String paymentSuccess(
			@RequestParam("paymentId") String paymentId,
			@RequestParam("payerId") String payerId
			) 
	{
		Payment payment = service.executePayment(paymentId, payerId);
		if(payment.getState().equals("approved")) {
			return "paymentSuccess";
		}
		return "paymentSuccess";
	}
	
	
	@GetMapping("/payment/error")
	public String paymentCancel() 
	{
		return "paymentSuccess";
	}
	
	@GetMapping("/payment/error")
	public String paymentError() 
	{
		return "paymentError";
	}
}
