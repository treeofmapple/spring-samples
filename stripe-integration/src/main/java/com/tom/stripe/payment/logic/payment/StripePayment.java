package com.tom.stripe.payment.logic.payment;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Refund;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.CustomerUpdateParams;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.PaymentIntentRetrieveParams;
import com.stripe.param.RefundCreateParams;
import com.tom.stripe.payment.exception.payment.PaymentException;
import com.tom.stripe.payment.exception.server.InternalException;
import com.tom.stripe.payment.payment.enums.AcceptedCurrency;
import com.tom.stripe.payment.payment.enums.PaymentMethods;
import com.tom.stripe.payment.user.model.User;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StripePayment {

	public PaymentIntent createPaymentIntent(BigDecimal amount, User user) {
		if (amount == null) {
			throw new PaymentException(String.format("Payment amount wasn't defined: %s", amount));
		}
	
		if (user.getCurrencyPreffered() == null) {
			throw new PaymentException(
					String.format("User didn't defined preffered currency: %s", user.getCurrencyPreffered()));
		}
		
		try {
			PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
				.setAmount(amount.longValue())
				.setCurrency(user.getCurrencyPreffered().getCurrencyString())
				.setCustomer(user.getStripeCustomerId())
				.addPaymentMethodType(user.getDefaultPaymentMethods().getPaymentMethodString())
				.setReceiptEmail(user.getEmail())
					.setAutomaticPaymentMethods(
							PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
							.setEnabled(true)
							.build())
				.build();
			return PaymentIntent.create(params);
		} catch (StripeException e) {
			throw new InternalException(String.format("System was unable to create payment intent for user: %s, cause {}", user.getEmail(), e.getMessage()));
		}
	}

	public PaymentIntent createCustomPaymentIntent(BigDecimal amount, User user, AcceptedCurrency currency, PaymentMethods paymentMethod) {
		if (amount == null) {
			throw new PaymentException(String.format("Payment amount wasn't defined: %s", amount));
		}
	
		try {
			PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
				.setAmount(amount.longValue())
				.setCurrency(currency.getCurrencyString())
				.setCustomer(user.getStripeCustomerId())
				.addPaymentMethodType(paymentMethod.getPaymentMethodString())
				.setReceiptEmail(user.getEmail())
					.setAutomaticPaymentMethods(
							PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
							.setEnabled(true)
							.build())
				.build();
			return PaymentIntent.create(params);
		} catch (StripeException e) {
			throw new InternalException(String.format("System was unable to create payment intent for user: %s, cause {}", user.getEmail(), e.getMessage()));
		}
	}

	public Refund refundPayment(String paymentIntentId) {
		try {
			RefundCreateParams params = RefundCreateParams.builder()
					.setPaymentIntent(paymentIntentId)
					.build();
			return Refund.create(params);
		} catch (StripeException e) {
			throw new InternalException(
					String.format("System was unable to make a refund for payment: %s, cause {}", paymentIntentId, e.getMessage()));
		}
	}
	
	public Refund refundPayment(String paymentIntentId, User user) {
		try {
			RefundCreateParams params = RefundCreateParams.builder()
					.setPaymentIntent(paymentIntentId)
					.build();
			return Refund.create(params);
		} catch (StripeException e) {
			throw new InternalException(
					String.format("System was unable to make a refund for user: %s, cause: %s", user.getEmail(), e.getMessage()));
		}
	}
	
	public String getReceiptUrl(String paymentIntentId) {
		try {
			PaymentIntentRetrieveParams params = PaymentIntentRetrieveParams.builder()
		            .addExpand("latest_charge")
		            .build();
	        PaymentIntent intent = PaymentIntent.retrieve(paymentIntentId, params, null);
	        
	        Charge charge = intent.getLatestChargeObject();
			if (charge != null && charge.getReceiptUrl() != null) {
				return charge.getReceiptUrl(); 
			}
			throw new PaymentException(
					String.format("Payment url: %s, don't have any payment attached to it.", paymentIntentId));
		} catch (StripeException e) {
			throw new InternalException(
					String.format("System was unable to fetch an reciept url: {0}, for this payment, cause: {1}", paymentIntentId, e.getMessage()));
		}
	}
	
	public String createStripeCustomer(User user) {
		try {
			CustomerCreateParams params = CustomerCreateParams.builder()
		        .setEmail(user.getEmail())
		        .setName(user.getNickname())
		        .putMetadata("internal_id", user.getId().toString())
		        .putMetadata("created_at", String.valueOf(System.currentTimeMillis()))
		        .build();
		    Customer customer = Customer.create(params);
		    return customer.getId();
		} catch(StripeException e) {
			throw new InternalException(String.format("System was unable to create user: %s, cause: %s", user.getEmail(), e.getMessage()));
		}
	}
	
	public void updateStripeCustomer(String stripeCustomerId, User user) {
	    try {
	        Customer customer = Customer.retrieve(stripeCustomerId);
	        
	        CustomerUpdateParams params = CustomerUpdateParams.builder()
	                .setEmail(user.getEmail())
	                .setName(user.getNickname())
	                .putMetadata("updated_at", String.valueOf(System.currentTimeMillis()))
	                .build();

	        customer.update(params);
	    } catch (StripeException e) {
			throw new InternalException(String.format("System was unable to update Stripe client: %s, cause: %s ",
					stripeCustomerId, e.getMessage()));
	    }
	}
	
	public boolean deleteStripeCustomer(String stripeCustomerId) {
		try {
			Customer customer = Customer.retrieve(stripeCustomerId);
			Customer deletedCustomer = customer.delete();
			return deletedCustomer.getDeleted();
		} catch (StripeException e) {
			throw new InternalException(
					String.format("System was unable to delete the user cause: %s", e.getMessage()));
		}
	}
	
}
