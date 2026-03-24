package com.tom.sample.paypal.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Service;

import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaypalService 
{

	private final APIContext apiContext;
	private final Transaction transaction;
	private final RedirectUrls redirect;
	private final Amount amount;
	private final Payer payer;
	private final Payment payment;
	private final PaymentExecution execution;
	private List<Transaction> transactions = new ArrayList<>();
	
	public Payment createPayment(
			Double total, String currency, 
			String method, String intent, 
			String description, String cancelURL,
			String successURL
			) 
	{
		log.info("Creating payment");
		
		amount.setCurrency(currency);
		amount.setTotal(String.format(Locale.forLanguageTag(currency), "%.2f", total));
		
		transaction.setDescription(description);
		transaction.setAmount(amount);
		transactions.add(transaction);
		
		payer.setPaymentMethod(method);

		payment.setIntent(intent);
		payment.setPayer(payer);
		payment.setTransactions(transactions);
		
		redirect.setCancelUrl(cancelURL);
		redirect.setReturnUrl(successURL);
		
		payment.setRedirectUrls(redirect);
		
		try {
			return payment.create(apiContext);
		} catch(PayPalRESTException e) {
			log.warn(" " + e);
		}
		
		return null;
	}
	
	public Payment executePayment(
			String paymentId,
			String payerId
			) 
	{
		payment.setId(payerId);
		execution.setPayerId(payerId);
		try {
			return payment.execute(apiContext, execution);
		} catch(PayPalRESTException e) {
			log.warn(" " + e);
		}
		return null;
	}
	
}
