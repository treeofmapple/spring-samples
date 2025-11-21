package com.tom.first.elastic.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.tom.first.elastic.models.evaluation.EvaluationDocument;

@Repository
public interface EvaluationSearchRepository extends ElasticsearchRepository<EvaluationDocument, String> {

}
