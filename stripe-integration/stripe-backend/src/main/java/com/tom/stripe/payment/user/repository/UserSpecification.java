package com.tom.stripe.payment.user.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.tom.stripe.payment.user.model.User;

import jakarta.persistence.criteria.Predicate;

@Component
public class UserSpecification {

	public static Specification<User> findByCriteria(String nickname, String email) {
		return (root, query, criteriaBuilder) -> {
			List<Predicate> predicates = new ArrayList<>();

			if (nickname != null && !nickname.trim().isEmpty()) {
				predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("nickname")),
						"%" + nickname.toLowerCase() + "%"));
			}

			if (email != null && !email.trim().isEmpty()) {
				predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("email")),
						"%" + email.toLowerCase() + "%"));
			}

			if (predicates.isEmpty()) {
				return criteriaBuilder.conjunction();
			}

			return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
		};
	}

}
