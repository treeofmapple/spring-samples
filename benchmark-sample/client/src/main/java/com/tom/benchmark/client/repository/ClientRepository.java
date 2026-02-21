package com.tom.benchmark.client.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import com.tom.benchmark.client.model.Client;

import jakarta.transaction.Transactional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

	Optional<Client> findByCpf(String cpf);
	
	boolean existsByCpf(String cpf);
	
	@Modifying
	@Transactional
	void deleteByCpf(String cpf);
	
}
