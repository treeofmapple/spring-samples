package com.tom.front.basic.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.tom.front.basic.model.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {

	Optional<Book> findByIsbn(String isbn);

	Optional<Book> findByTitle(String title);
	
	boolean existsByIsbn(String isbn);
	
	boolean existsByTitle(String title);
	
	boolean existsByTitleOrIsbn(String title, String isbn);
	
}
