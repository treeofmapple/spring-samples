package com.tom.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tom.management.model.EspacoFisico;

@Repository
public interface EspacoRepository extends JpaRepository<EspacoFisico, Long> {

	boolean existsByNomeAndTipo(String nome, String tipo);

	boolean existsByNome(String nome);

}
