package com.tom.stripe.payment.payment.service;

import java.time.ZonedDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tom.stripe.payment.dto.RefundRequest;
import com.tom.stripe.payment.exception.payment.PaymentException;
import com.tom.stripe.payment.history.component.PaymentHistoryComponent;
import com.tom.stripe.payment.logic.payment.StripePayment;
import com.tom.stripe.payment.logic.security.SecurityUtils;
import com.tom.stripe.payment.payment.component.PaymentComponent;
import com.tom.stripe.payment.payment.dto.PagePaymentResponse;
import com.tom.stripe.payment.payment.dto.PaymentRequest;
import com.tom.stripe.payment.payment.dto.PaymentResponse;
import com.tom.stripe.payment.payment.dto.PaymentUpdate;
import com.tom.stripe.payment.payment.enums.PaymentMethods;
import com.tom.stripe.payment.payment.enums.PaymentStatus;
import com.tom.stripe.payment.payment.mapper.PaymentMapper;
import com.tom.stripe.payment.payment.repository.PaymentRepository;
import com.tom.stripe.payment.payment.repository.filtering.PaymentSortOption;
import com.tom.stripe.payment.payment.repository.filtering.PaymentSortParameter;
import com.tom.stripe.payment.user.component.UserComponent;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class PaymentService {

	@Value("${application.size.page:20}")
	private int PAGE_SIZE;

	private final PaymentRepository repository;
	private final PaymentMapper mapper;
	private final PaymentSortParameter paymentSort;
	
	private final PaymentComponent component;
	private final PaymentHistoryComponent historyComponent;
	
	private final UserComponent userComponent;
	
	private final StripePayment stripePayment;
	
	private final SecurityUtils security;
	
	/* filter these payments for only made by the user on a production system */
	
	@Transactional(readOnly = true)
	public PagePaymentResponse searchPaymentByParams(int page, PaymentSortOption sortParam) {
		var finalSort = paymentSort.selectPaymentSort(sortParam);
		Pageable pageable = PageRequest.of(page, PAGE_SIZE, finalSort);
		var payment = repository.findAll(pageable);
		return mapper.toResponse(payment);
	}

	@Transactional(readOnly = true)
	public PaymentResponse searchPaymentById(UUID paymentId) {
		var payment = component.findById(paymentId);
		return mapper.toResponse(payment);
	} 
	
	@Transactional
	public PaymentResponse performPayment() {
		/*
		
		try {
			
		} catch (StripeException e) {
			// payment.setStatus(PaymentStatus.PAYMENT_CANCELLED);
		}
		
		*/
	}
	
	public void processUserRefund() {
		
	}
	
	@Transactional
	public PaymentResponse requireRefund(RefundRequest request) {
		return null;
	}
	
	@Transactional
	public PaymentResponse createPayment(PaymentRequest request) {
		var userIp = security.getRequestingClientIp();
		log.info("IP: {}, is generating a payment.", userIp);

		var user = userComponent.findById(request.userId());
		
		if (user.getTaxId() == null || user.getPostalCode() == null || user.getCountryCode() == null) {
			throw new PaymentException(String.format("User didn't fill their taxId, postalCode or countryCode"));
		}

		var payment = mapper.build(request);
		payment.setUserId(user.getId());
		var stripe = stripePayment.createPaymentIntent(request.amount(), user);
		payment.setStripePaymentIntentId(stripe.getId());

		if (payment.getPaymentMethod() == PaymentMethods.PIX) {
			payment.setStatus(PaymentStatus.REQUIRED_ACTION);
			historyComponent.buildHistory(payment, PaymentStatus.REQUIRED_ACTION);
		} else {
			payment.setStatus(PaymentStatus.REQUIRES_PAYMENT_METHOD);
			historyComponent.buildHistory(payment, PaymentStatus.REQUIRES_PAYMENT_METHOD);
		}
		
		var savedPayment = repository.save(payment);
		return mapper.toResponse(savedPayment);
	}
	
	@Transactional
	public PaymentResponse updatePayment(PaymentUpdate request) {
		return null;
	}

	@Transactional
	public void deletePayment(UUID query) {
		var payment = component.findById(query);
		payment.setActive(false);
		payment.setDeletedAt(ZonedDateTime.now());
		payment.setStatus(PaymentStatus.PAYMENT_CANCELLED);
		historyComponent.buildHistory(payment, PaymentStatus.PAYMENT_CANCELLED, null);
		repository.save(payment);
	}

	
	/* require a refund 
	 *
	 * {
	 * i will create two endpoints, one for easy one
	 * other for system auditory, only allow if the system permits it,
	 * user check refund 
	 * }
	 * */
	
}
