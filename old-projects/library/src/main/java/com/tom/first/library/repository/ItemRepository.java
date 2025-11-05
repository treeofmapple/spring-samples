package com.tom.first.library.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import com.tom.first.library.model.Book;
import com.tom.first.library.model.BookItem;
import com.tom.first.library.model.User;
import com.tom.first.library.model.enums.Status;

import jakarta.transaction.Transactional;

@Repository
public interface ItemRepository extends JpaRepository<BookItem, Long> {

	int countByUserIdAndBookIdAndStatus(UUID userId, Long bookId, Status Status);
	
	List<BookItem> findByUser(UUID userId);

	Optional<BookItem> findByBookName(String bookName);
	
	boolean existsByBookAndUser(Book book, User user);
	
    @Modifying
    @Transactional
	void deleteByBookAndUser(Book book, User user);
	
}
