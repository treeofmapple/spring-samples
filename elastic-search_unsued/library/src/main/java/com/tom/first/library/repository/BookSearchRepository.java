package com.tom.first.library.repository;

import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tom.first.library.model.BookDocument;

@Repository
public interface BookSearchRepository extends ElasticsearchRepository<BookDocument, String> {

	boolean existsByTitle(String title);

	@Modifying
	@Transactional
	void deleteByTitle(String title);

	
	@Modifying
	@Transactional
	@Query("{\"query\": {\"term\": {\"title\": \"?0\"}}}")
	long deleteByTitleReturningCount(String title);

}
