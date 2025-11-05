package com.tom.management.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tom.management.model.Feriado;

public interface FeriadoRepository extends JpaRepository<Feriado, Long> {

	boolean existsByNomeAndData(String nome, LocalDate data);

	boolean existsByNome(String nome);

}
