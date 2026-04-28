package br.tekk.system.library.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.tekk.system.library.model.User;

public interface UserRepository extends JpaRepository<User, UUID> {

	boolean existsByEmail(String email);
}
