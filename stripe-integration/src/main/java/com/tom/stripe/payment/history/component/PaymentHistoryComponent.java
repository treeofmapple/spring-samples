package com.tom.stripe.payment.history.component;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.tom.stripe.payment.exception.sql.NotFoundException;
import com.tom.stripe.payment.history.dto.PagePaymentHistoryResponse;
import com.tom.stripe.payment.history.dto.PaymentHistoryResponse;
import com.tom.stripe.payment.history.mapper.PaymentHistoryMapper;
import com.tom.stripe.payment.history.model.PaymentHistory;
import com.tom.stripe.payment.history.repository.PaymentHistoryRepository;
import com.tom.stripe.payment.payment.enums.PaymentStatus;
import com.tom.stripe.payment.payment.model.Payment;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PaymentHistoryComponent {

	@Value("${application.size.page:20}")
	private int PAGE_SIZE;
	
	private final PaymentHistoryRepository repository;
	private final PaymentHistoryMapper mapper;
	
	@Transactional(readOnly = true)
	public PaymentHistoryResponse findById(UUID paymentHistoryId) {
		var history = repository.findById(paymentHistoryId).orElseThrow(() -> new NotFoundException(
				String.format("Payment History with id %s, was not found.", paymentHistoryId)));
		return mapper.toResponse(history);
	}

	@Transactional(readOnly = true)
	public PagePaymentHistoryResponse findByParams(int page) {
		Pageable pageable = PageRequest.of(page, PAGE_SIZE);
		var history = repository.findAll(pageable);
		return mapper.toResponse(history);
	}
	
	@Transactional
	public void buildHistory(Payment payments, PaymentStatus status, String reason) {
		var history = PaymentHistory.builder()
				.payment(payments)
				.status(status)
				.reason(reason)
				.build();
		repository.save(history);
	}
	
}
