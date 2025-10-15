package com.tom.aws.kafka.book;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import jakarta.persistence.criteria.Predicate;

@Component
public class BookSpecification {

	private BookSpecification() {}
	
	public static Specification<Book> findByCriteria(String isbn, String title, String author, LocalDate startDate, LocalDate endDate) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(isbn != null && !isbn.isBlank()) {
            	predicates.add(cb.like(cb.lower(root.get("isbn")), "%" + isbn.toLowerCase() + "%"));
            }
            
            if (title != null && !title.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%"));
            }

            if (author != null && !author.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("author")), "%" + author.toLowerCase() + "%"));
            }

            if (startDate != null && endDate != null) {
                predicates.add(cb.between(root.get("publishedDate"), startDate, endDate));
            } else if (startDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("publishedDate"), startDate));
            } else if (endDate != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("publishedDate"), endDate));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
	}
	
}
