package com.tom.benchmark.monolith.product;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

	Optional<Product> findBySku(String name);
	
	boolean existsBySku(String sku);

	@Modifying
	@Transactional
	void deleteBySku(String sku);
	
}
