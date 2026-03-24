package com.tom.first.vehicle.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tom.first.vehicle.model.Brand;
import com.tom.first.vehicle.model.Model;

@Repository
public interface ModelRepository extends JpaRepository<Model, Long> {
	
	Optional<Model> findByNameAndBrand(String name, Brand brand);
	
	boolean existsByName(String Name);

	boolean existsByNameAndIdNot(String name, long id);
}
