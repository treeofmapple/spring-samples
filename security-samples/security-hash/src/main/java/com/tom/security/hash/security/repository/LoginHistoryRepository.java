package com.tom.security.hash.security.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tom.security.hash.security.model.LoginHistory;

@Repository
public interface LoginHistoryRepository extends JpaRepository<LoginHistory, UUID> {

	Page<LoginHistory> findByUserIdOrderByLoginTimeDesc(UUID userId, Pageable pageable);
	
    Optional<LoginHistory> findFirstByUserIdOrderByLoginTimeDesc(UUID userId);

}
