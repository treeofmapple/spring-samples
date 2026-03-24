package com.tom.management.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tom.management.request.SolicitacaoRequest;
import com.tom.management.request.SolicitacaoResponse;
import com.tom.management.request.dto.SolicitacaoNomeRequestDTO;
import com.tom.management.request.dto.StatusDTO;
import com.tom.management.service.SolicitacaoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/solicitacoes")
@RequiredArgsConstructor
public class SolicitacaoController {

	private final SolicitacaoService service;

	@GetMapping("/get")
	public ResponseEntity<List<SolicitacaoResponse>> findAll() {
		return ResponseEntity.status(HttpStatus.OK).body(service.findAll());
	}

	@GetMapping("/get/{id}")
	public ResponseEntity<SolicitacaoResponse> findById(@PathVariable Long id) {
		return ResponseEntity.status(HttpStatus.OK).body(service.findById(id));
	}

	@GetMapping("/aprovadas")
	public ResponseEntity<List<SolicitacaoResponse>> findAprovadas() {
		return ResponseEntity.status(HttpStatus.OK).body(service.findAprovadas());
	}

	@PostMapping("/create")
	public ResponseEntity<Long> createSolicitacao(@RequestBody @Valid SolicitacaoRequest request) {
		Long solicitacaoId = service.createSolicitacao(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(solicitacaoId);
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<Void> updateSolicitacao(@PathVariable Long id,
			@RequestBody @Valid SolicitacaoRequest request) {
		service.updateSolicitacao(id, request);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@PutMapping("/cancelar")
	public ResponseEntity<Void> cancelarSolicitacao(@RequestBody @Valid SolicitacaoNomeRequestDTO request) {
		service.cancelarSolicitacao(request);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@PutMapping("/avaliar/{id}/{AvaliadorID}")
	public ResponseEntity<SolicitacaoResponse> avaliarSolicitacao(@PathVariable Long id,
			@RequestBody @Valid StatusDTO request, @PathVariable Long AvaliadorID) {
		service.avaliarSolicitacao(id, request, AvaliadorID);
		return ResponseEntity.status(HttpStatus.OK).build();
	} 

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> deleteSolicitacao(@PathVariable Long id) {
		service.deleteSolicitacao(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
