package com.tom.first.simple.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tom.first.simple.model.EvaluationOutbox;

@Repository
public interface EvaluationOutboxRepository extends JpaRepository<EvaluationOutbox, Long> {

	List<EvaluationOutbox> findByProcessedFalse();
	
}
