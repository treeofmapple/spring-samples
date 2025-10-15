package com.tom.auth.monolithic.user.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tom.auth.monolithic.user.model.LoginHistory;

@Repository
public interface LoginHistoryRepository extends JpaRepository<LoginHistory, UUID> {

	Page<LoginHistory> findByUserIdOrderByLoginTimeDesc(UUID userId, Pageable pageable);
	
    Page<LoginHistory> findByOrderByLoginTimeDesc(Pageable pageable);
	
    Optional<LoginHistory> findFirstByUserIdOrderByLoginTimeDesc(UUID userId);

}
