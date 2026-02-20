package com.tom.stripe.payment.user.repository.filtering;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class UserSortParameter {

	public Sort selectUserSort(UserSortOption sort) {
		if (sort == null) {
			return Sort.by("createdAt").descending();
		}

		return switch (sort) {
		case MOST_RECENT -> Sort.by("createdAt").descending();
		case NICKNAME -> Sort.by("nickname").ascending();
		case EMAIL -> Sort.by("email").ascending();
		};
	}

}
