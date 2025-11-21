package com.tom.first.username.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tom.first.username.model.UserOutbox;

@Repository
public interface UserOutboxRepository extends JpaRepository<UserOutbox, Long> {

	List<UserOutbox> findByProcessedFalse();
}
