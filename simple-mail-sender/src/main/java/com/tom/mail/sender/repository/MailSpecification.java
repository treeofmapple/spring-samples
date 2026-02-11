package com.tom.mail.sender.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.tom.mail.sender.model.Mail;

import jakarta.persistence.criteria.Predicate;

@Component
public class MailSpecification {

	public static Specification<Mail> findByCriteria(String title, List<String> users) {
		return (root, query, criteriaBuilder) -> {
			List<Predicate> predicates = new ArrayList<>();

			if (title != null && !title.trim().isEmpty()) {
				predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("title")),
						"%" + title.toLowerCase() + "%"));
			}

			if (users != null && !users.isEmpty()) {
				predicates.add(root.join("users").in(users));
				query.distinct(true);
			}

			return predicates.isEmpty() ? criteriaBuilder.conjunction()
					: criteriaBuilder.and(predicates.toArray(new Predicate[0]));
		};
	}

}
