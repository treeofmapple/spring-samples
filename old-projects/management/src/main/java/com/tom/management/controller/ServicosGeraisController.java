package com.tom.management.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tom.management.request.AvaliadorResponse;
import com.tom.management.request.EquipamentoRequest;
import com.tom.management.request.EquipamentoResponse;
import com.tom.management.request.FeriadoRequest;
import com.tom.management.request.FeriadoResponse;
import com.tom.management.service.ServicosGeraisService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/servicos")
@RequiredArgsConstructor
public class ServicosGeraisController {

	private final ServicosGeraisService service;
	
	@GetMapping("/get/avaliadores")
	public ResponseEntity<List<AvaliadorResponse>> findAllAvaliadores() {
		return ResponseEntity.status(HttpStatus.OK).body(service.findAllAvaliador());
	}
	
	@GetMapping("/get/feriado")
	public ResponseEntity<List<FeriadoResponse>> findAllFeriados(){
		return ResponseEntity.status(HttpStatus.OK).body(service.findAllFeriados());
	}
	
	@PostMapping("/create/feriado")
	public ResponseEntity<Long> createFeriado(@RequestBody @Valid FeriadoRequest request) {
		Long feriadoId = service.createFeriadoData(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(feriadoId);
	}
	
	@DeleteMapping("/delete/feriado/{id}")
	public ResponseEntity<Void> deleteFeriado(@PathVariable Long id){
		service.deleteFeriado(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	
	@GetMapping("/get/equipamento")
	public ResponseEntity<List<EquipamentoResponse>> findAllEquipamentos(){
		return ResponseEntity.status(HttpStatus.OK).body(service.findAllEquipamentos());
	}
	
	@PostMapping("/create/equipamento")
	public ResponseEntity<Long> createEquipamento(@RequestBody @Valid EquipamentoRequest request){
		Long equipamentoId = service.createEquipamento(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(equipamentoId);
	}
	
	@DeleteMapping("/delete/equipamento/{id}")
	public ResponseEntity<Void> deleteEquipamento(@PathVariable Long id){
		service.deleteEquipamento(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	
}
