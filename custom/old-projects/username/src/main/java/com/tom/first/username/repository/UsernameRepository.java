package com.tom.first.username.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import com.tom.first.username.model.Username;

public interface UsernameRepository extends JpaRepository<Username, Long> {

	boolean existsByEmail(String email);

	boolean existsByName(String name);

	@Modifying
	void deleteByName(String name);

	Optional<Username> findOptionalByName(String name);

	Username findByName(String name);

}
