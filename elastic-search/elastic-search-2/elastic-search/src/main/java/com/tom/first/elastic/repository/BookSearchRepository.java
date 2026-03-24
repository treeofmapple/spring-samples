package com.tom.first.elastic.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.tom.first.elastic.models.library.BookDocument;

@Repository
public interface BookSearchRepository extends ElasticsearchRepository<BookDocument, String> {

	Optional<BookDocument> findByTitle(String title);

	List<BookDocument> findByAuthor(String author);
	
	List<BookDocument> findByLaunchYearBetween(LocalDate start, LocalDate end);

	long countByAuthor(String author);

}
