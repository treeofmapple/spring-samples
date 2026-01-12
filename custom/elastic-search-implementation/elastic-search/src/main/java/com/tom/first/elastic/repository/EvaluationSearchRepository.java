package com.tom.first.elastic.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.tom.first.elastic.models.evaluation.EvaluationDocument;

@Repository
public interface EvaluationSearchRepository extends ElasticsearchRepository<EvaluationDocument, String> {

	Optional<EvaluationDocument> findBySubject(String subject);

	List<EvaluationDocument> findBySubjectContaining(String subject);

	List<EvaluationDocument> findByUsername(String username);

	long countByUsername(String username);

	
	// List<EvaluationDocument> findByGradeGreaterThanEqual(Double grade);
	
	// List<EvaluationDocument> findByGradeLessThanEqual(Double grade);

	// List<EvaluationDocument> findBySubjectAndUsername(String subject, String username);

	// List<EvaluationDocument> findBySubjectContainingAndUsername(String subject, String username);
	
}
