package com.tom.mail.sender.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.tom.mail.sender.model.Mail;

import jakarta.persistence.criteria.Predicate;

@Component
public class MailSpecialization {

	public static Specification<Mail> findByCriteria(String title) {
		return (root, query, criteriaBuilder) -> {
			List<Predicate> predicates = new ArrayList<>();

			if (title != null && !title.trim().isEmpty()) {
				predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("title")),
						"%" + title.toLowerCase() + "%"));
			}

			if (predicates.isEmpty()) {
				return criteriaBuilder.conjunction();
			}

			return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
		};
	}

}
