package com.tom.management.repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tom.management.model.Equipamento;

@Repository
public interface EquipamentoRepository extends JpaRepository<Equipamento, Long> {

	Optional<Equipamento> findByNome(String nome);

	boolean existsByNome(String nome);

	boolean existsByNomeIn(Set<String> equipamento);

	Set<Equipamento> findByNomeIn(Set<String> equipamento);

}
