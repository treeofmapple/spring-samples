package com.tom.aws.awstest.product;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

	@Query("SELECT i FROM Product i ORDER BY i.createdAt DESC")
	Page<Product> findMostRecent(Pageable pageable);
	
	Page<Product> findById(Long id, Pageable pageable);
	
	Page<Product> findByName(String name, Pageable pageable);
	
	Optional<Product> findByName(String name);

	boolean existsByName(String name);
	
	boolean existsByNameAndIdNot(String name, Long id);

}
