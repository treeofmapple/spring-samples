package com.tom.first.simple.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import com.tom.first.simple.model.Evaluation;

import jakarta.transaction.Transactional;

@Repository
public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {

	Optional<Evaluation> findBySubject(String subject);
	
	List<Evaluation> findByGrade(double grade);

	boolean existsBySubject(String subject);

    @Modifying
    @Transactional
	void deleteBySubject(String subject);
    
}
