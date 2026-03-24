package com.tom.first.library.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import com.tom.first.library.model.User;

import jakarta.transaction.Transactional;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

	boolean existsByEmail(String email);

    @Modifying
    @Transactional
	void deleteByEmail(String email);

	List<User> findAllByUsername(String name);
	
	Optional<User> findByUsername(String name);
	
	Optional<User> findByEmail(String email);

}
