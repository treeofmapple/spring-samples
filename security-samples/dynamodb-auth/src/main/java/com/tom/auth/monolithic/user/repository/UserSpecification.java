package com.tom.auth.monolithic.user.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.tom.auth.monolithic.user.model.User;
import com.tom.auth.monolithic.user.model.enums.Role;

import jakarta.persistence.criteria.Predicate;

@Component
public class UserSpecification {

	public static Specification<User> findByCriteria(String username, String email, Integer age) {
		return(root, query, criteriaBuilder) ->{
			List<Predicate> predicates = new ArrayList<>();
			
            if (username != null && !username.trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("username")),
                        "%" + username.toLowerCase() + "%"));
            }

            if (email != null && !email.trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("email")),
                        "%" + email.toLowerCase() + "%"));
            }

            if (age != null) {
                predicates.add(criteriaBuilder.equal(root.get("age"), age));
            }

			if (predicates.isEmpty()) {
				return criteriaBuilder.conjunction();
			}
			
			return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
		};
	}
	
	public static Specification<User> findByCriteria(String username, String email, Integer age, Role roles, Boolean accountLocked) {
		return(root, query, criteriaBuilder) ->{
			List<Predicate> predicates = new ArrayList<>();
			
            if (username != null && !username.trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("username")),
                        "%" + username.toLowerCase() + "%"));
            }

            if (email != null && !email.trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("email")),
                        "%" + email.toLowerCase() + "%"));
            }

            if (age != null) {
                predicates.add(criteriaBuilder.equal(root.get("age"), age));
            }

            if (roles != null) {
                predicates.add(criteriaBuilder.equal(root.get("role"), roles));
            }
            
            if (accountLocked != null) {
                predicates.add(criteriaBuilder.equal(root.get("accountNonLocked"), accountLocked));
            }
			
			if (predicates.isEmpty()) {
				return criteriaBuilder.conjunction();
			}
			
			return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
		};
	}
	
}
