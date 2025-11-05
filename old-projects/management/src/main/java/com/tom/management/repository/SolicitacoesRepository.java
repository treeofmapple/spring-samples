package com.tom.management.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tom.management.model.Solicitacao;
import com.tom.management.model.Status;

@Repository
public interface SolicitacoesRepository extends JpaRepository<Solicitacao, Long> {

	List<Solicitacao> findByStatus(Status aprovada);

	Optional<Solicitacao> findByNome(String nome);

}
