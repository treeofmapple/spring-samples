package com.tom.front.full.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.tom.front.full.model.Employee;

import jakarta.persistence.criteria.Predicate;

@Component
public class EmployeeSpecification {

	public static Specification<Employee> findByCriteria(String name, String email, String jobTitle,
			String employeeCode) {
		return (root, query, criteriaBuilder) -> {
			List<Predicate> predicates = new ArrayList<>();

			if (name != null && !name.trim().isEmpty()) {
				predicates.add(
						criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
			}

			if (email != null && !email.trim().isEmpty()) {
				predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("email")),
						"%" + email.toLowerCase() + "%"));
			}

			if (jobTitle != null && !jobTitle.trim().isEmpty()) {
				predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("jobTitle")),
						"%" + jobTitle.toLowerCase() + "%"));
			}

			if (employeeCode != null && !employeeCode.trim().isEmpty()) {
				predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("employeeCode")),
						"%" + employeeCode.toLowerCase() + "%"));
			}

			return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
		};
	}

}
