package com.tom.first.simple.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tom.first.simple.model.EvaluationDocument;

@Repository
public interface EvaluationSearchRepository extends ElasticsearchRepository<EvaluationDocument, String> {

	boolean existsBySubject(String subject);
	
	@Modifying
	@Transactional
	void deleteBySubject(String subject);
	
}
