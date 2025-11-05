package com.tom.first.establishment.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tom.first.establishment.model.Establishment;

@Repository
public interface EstablishmentRepository extends JpaRepository<Establishment, Long> {

	Optional<Establishment> findByName(String name);

	Optional<Establishment> findByCnpj(String cnpj);

	boolean existsByNameAndCnpj(String name, String cnpj);

	boolean existsByName(String name);

	void deleteByName(String name);

}
