package com.tom.first.library.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import com.tom.first.library.model.Book;

import jakarta.transaction.Transactional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

	boolean existsByTitle(String title);

    @Modifying
    @Transactional
	void deleteByTitle(String title);
	
	Optional<Book> findByTitle(String title);

	List<Book> findByAuthor(String author);

	List<Book> findByLaunchYearBetween(LocalDate startDate, LocalDate endDate);
}
