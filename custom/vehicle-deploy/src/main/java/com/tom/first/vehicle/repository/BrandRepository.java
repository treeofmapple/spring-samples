package com.tom.first.vehicle.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tom.first.vehicle.model.Brand;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {

	Optional<Brand> findByName(String name);
	
	boolean existsByName(String name);
	
	boolean existsByNameAndIdNot(String name, long id);
	
}
