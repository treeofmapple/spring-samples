package com.tom.first.simple.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.tom.first.simple.model.User;

import jakarta.transaction.Transactional;

public interface UserRepository extends JpaRepository<User, Long> {

	boolean existsByName(String name);

	boolean existsByEmail(String email);

    @Modifying
    @Transactional
	void deleteByName(String name);

	Optional<User> findByName(String name);

	@Query("SELECT u FROM User u LEFT JOIN FETCH u.evaluations")
	List<User> findAllUsersWithEvaluations();
	
}
