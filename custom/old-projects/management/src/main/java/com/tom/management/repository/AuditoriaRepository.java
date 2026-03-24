package com.tom.management.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tom.management.model.Auditoria;
import com.tom.management.model.Usuario;

@Repository
public interface AuditoriaRepository extends JpaRepository<Auditoria, Long> {

	boolean existsByUsuarioAndAcaoAndDataBetween(Usuario usuario, String acao, LocalDateTime windowStart, LocalDateTime windowEnd);

}
