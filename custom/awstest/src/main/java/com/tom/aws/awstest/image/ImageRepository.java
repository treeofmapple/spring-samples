package com.tom.aws.awstest.image;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

	@Query("SELECT i FROM Image i ORDER BY i.createdAt DESC")
	Page<Image> findMostRecent(Pageable pageable);
	
	Page<Image> findById(Long id, Pageable pageable);
	
	Page<Image> findByName(String name, Pageable pageable);
	
	Optional<Image> findByName(String name);
	
	boolean existsByName(String name);
	
	boolean existsByNameAndIdNot(String name, Long id);
	
}
