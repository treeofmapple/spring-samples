package com.tom.first.vehicle.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.tom.first.vehicle.model.Vehicle;

import jakarta.persistence.criteria.Predicate;

@Component
public class VehicleSpecification {

	public static Specification<Vehicle> findByCriteria(String brand, String model, String color, String plate) {
		return(root, query, criteriaBuilder) -> {
			List<Predicate> predicates = new ArrayList<>();

			if (brand != null && !brand.trim().isEmpty()) {
				predicates.add(
						criteriaBuilder.like(
								criteriaBuilder.lower(root.get("brand").get("name")),
								"%" + brand.toLowerCase() + "%"));
			}

			if (model != null && !model.trim().isEmpty()) {
				predicates.add(
						criteriaBuilder.like(
								criteriaBuilder.lower(root.get("model").get("name")),
								"%" + model.toLowerCase() + "%"));
			}
			
			if(color != null && !color.trim().isEmpty()) {
				predicates.add(
						criteriaBuilder.like(
								criteriaBuilder.lower(root.get("color")),
                        "%" + color.toLowerCase() + "%"));
			}
			
			if(plate != null && !plate.trim().isEmpty()) {
				predicates.add(
						criteriaBuilder.like(
								criteriaBuilder.lower(root.get("plate")),
                        "%" + plate.toLowerCase() + "%"));
			}
			
			if(predicates.isEmpty()) {
				return criteriaBuilder.conjunction();
			}
			
			return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
		};
	}
	
}
