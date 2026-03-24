package com.tom.first.library.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tom.first.library.model.BookOutbox;

public interface BookOutboxRepository extends JpaRepository<BookOutbox, Long> {

	List<BookOutbox> findByProcessedFalse();
}
