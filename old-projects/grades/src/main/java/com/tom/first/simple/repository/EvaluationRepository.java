package com.tom.first.simple.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import com.tom.first.simple.model.Evaluation;

import jakarta.transaction.Transactional;

public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {

	boolean existsBySubject(String subject);

	boolean existsByGrade(double grade);

    @Modifying
    @Transactional
	void deleteBySubject(String subject);

	Optional<Evaluation> findBySubject(String subject);

	List<Evaluation> findByGrade(double grade);

}
