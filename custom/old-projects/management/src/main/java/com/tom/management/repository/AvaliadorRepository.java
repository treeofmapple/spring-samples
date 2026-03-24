package com.tom.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tom.management.model.Avaliador;

@Repository
public interface AvaliadorRepository extends JpaRepository<Avaliador, Long> {

}
