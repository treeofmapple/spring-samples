package com.tom.first.library.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import com.tom.first.library.model.Book;

import jakarta.transaction.Transactional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

	Optional<Book> findByTitle(String title);

	List<Book> findByAuthor(String author);

	boolean existsByTitle(String title);

	@Modifying
	@Transactional
	void deleteByTitle(String title);

}
