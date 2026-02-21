package com.tom.stripe.payment.payment.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tom.stripe.payment.history.component.PaymentHistoryComponent;
import com.tom.stripe.payment.logic.security.SecurityUtils;
import com.tom.stripe.payment.payment.component.PaymentComponent;
import com.tom.stripe.payment.payment.dto.PagePaymentResponse;
import com.tom.stripe.payment.payment.dto.PaymentResponse;
import com.tom.stripe.payment.payment.mapper.PaymentMapper;
import com.tom.stripe.payment.payment.repository.PaymentRepository;
import com.tom.stripe.payment.payment.repository.filtering.PaymentSortOption;
import com.tom.stripe.payment.payment.repository.filtering.PaymentSortParameter;

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
	
	private final SecurityUtils security;
	
	@Transactional(readOnly = true)
	public PagePaymentResponse searchUserByParams(int page, PaymentSortOption sortParam) {
		var finalSort = paymentSort.selectPaymentSort(sortParam);
		Pageable pageable = PageRequest.of(page, PAGE_SIZE, finalSort);
		var payment = repository.findAll(pageable);
		return mapper.toResponse(payment);
	}

	@Transactional(readOnly = true)
	public PaymentResponse searchById(UUID paymentId) {
		var payment = component.findById(paymentId);
		return mapper.toResponse(payment);
	} 
	
	// set currency
	
	// set user default payment method
	
	// fetch all
	
	// fetch by id
	
	// add stripe tax (compliance)
	
	// create a payment
	
	// update a payment if needs
	
	// delete a payment
	
	/* require a refund 
	 *
	 * {
	 * i will create two endpoints, one for easy one
	 * other for system auditory, only allow if the system permits it,
	 * user check refund 
	 * }
	 * */
	
}
