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

import com.tom.management.request.EquipamentoRequest;
import com.tom.management.request.EspacoRequest;
import com.tom.management.request.EspacoResponse;
import com.tom.management.request.dto.DisplayEquipamentoDTO;
import com.tom.management.request.dto.DisponibilidadeDTO;
import com.tom.management.request.dto.EspacoRequestDTO;
import com.tom.management.service.EspacoFisicoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/espaco")
@RequiredArgsConstructor
public class EspacoFisicoController {

	private final EspacoFisicoService service;

	@GetMapping("/get")
	public ResponseEntity<List<EspacoResponse>> findAll() {
		return ResponseEntity.status(HttpStatus.OK).body(service.findAll());
	}

	@GetMapping("/get/{id}")
	public ResponseEntity<EspacoResponse> findById(@PathVariable Long id) {
		return ResponseEntity.status(HttpStatus.OK).body(service.findById(id));
	}

	@GetMapping("/equipamento/{id}")
	public ResponseEntity<DisplayEquipamentoDTO> listarEquipamentos(@PathVariable Long id) {
		return ResponseEntity.status(HttpStatus.OK).body(service.displayEquipamentos(id));
	}

	@GetMapping("/disponibilidade/{id}")
	public ResponseEntity<DisponibilidadeDTO> verificarDisponibilidade(@PathVariable Long id) {
		return ResponseEntity.status(HttpStatus.OK).body(service.verificarDisponibilidade(id));
	}

	@PostMapping("/create")
	public ResponseEntity<Long> createEspaco(@RequestBody @Valid EspacoRequest request) {
		Long espacoId = service.createEspaco(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(espacoId);
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<Void> updateEspaco(@PathVariable Long id, @RequestBody @Valid EspacoRequestDTO request) {
		service.updateEspaco(id, request);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@PutMapping("/inserirEquipamento/{id}")
	public ResponseEntity<Void> inserirEquipamento(@PathVariable Long id, @RequestBody @Valid EquipamentoRequest request){
		service.inserirEquipamento(id, request);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	
	@PutMapping("/disponivel/{id}")
	public ResponseEntity<Void> disponivelEspaco(@PathVariable Long id){
		service.disponivelEspaco(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@PutMapping("/indisponivel/{id}")
	public ResponseEntity<Void> indisponivelEspaco(@PathVariable Long id){
		service.indisponivelEspaco(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> deleteUsuario(@PathVariable Long id) {
		service.deleteEspaco(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

}
