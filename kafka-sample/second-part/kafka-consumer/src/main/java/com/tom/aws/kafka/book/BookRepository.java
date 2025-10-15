package com.tom.aws.kafka.book;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {

	Book findByIsbn(String isbn);

	Optional<Book> findByTitle(String title);
	
	boolean existsByIsbn(String isbn);
	
	boolean existsByTitle(String title);
	
	boolean existsByTitleOrIsbn(String title, String isbn);
	
}
