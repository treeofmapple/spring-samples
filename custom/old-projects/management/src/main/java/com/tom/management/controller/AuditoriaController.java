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

import com.tom.management.request.AuditoriaRequest;
import com.tom.management.request.AuditoriaResponse;
import com.tom.management.service.AuditoriaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/auditoria")
@RequiredArgsConstructor
public class AuditoriaController {

	private final AuditoriaService service;
	
	@GetMapping("/get")
	public ResponseEntity<List<AuditoriaResponse>> findAll(){
		return ResponseEntity.status(HttpStatus.OK).body(service.findAll());
	}
	
	@GetMapping("/get/{id}")
	public ResponseEntity<AuditoriaResponse> findById(@PathVariable Long id){
		return ResponseEntity.status(HttpStatus.OK).body(service.findById(id));
	} 
	
	@PostMapping("/create")
	public ResponseEntity<Long> createAuditoria(@RequestBody @Valid AuditoriaRequest request){
		Long usuarioId = service.createAuditoria(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(usuarioId);
	}
	
	@PutMapping("/update/{id}")
	public ResponseEntity<Void> updateAuditoria(@PathVariable Long id, @RequestBody @Valid AuditoriaRequest request){
		service.updateAuditoria(id, request);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> deleteAuditoria(@PathVariable Long id){
		service.deleteAuditoria(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	
	
	
}
