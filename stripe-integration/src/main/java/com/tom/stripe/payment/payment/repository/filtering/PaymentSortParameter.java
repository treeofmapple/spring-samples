package com.tom.stripe.payment.payment.repository.filtering;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class PaymentSortParameter {

	public Sort selectPaymentSort(PaymentSortOption sort) {
		if (sort == null) {
			return Sort.by("createdAt").descending();
		}

		return switch (sort) {
		case MOST_RECENT -> Sort.by("createdAt").descending();
		case AMOUNT -> Sort.by("amount").descending();
		case CURRENCY -> Sort.by("currency").descending();
		};
	}

}
