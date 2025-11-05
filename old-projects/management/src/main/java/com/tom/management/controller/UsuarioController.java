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

import com.tom.management.request.UsuarioRequest;
import com.tom.management.request.UsuarioResponse;
import com.tom.management.service.UsuarioService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/usuario")
@RequiredArgsConstructor
public class UsuarioController {

	private final UsuarioService service;
	
	@GetMapping("/get")
	public ResponseEntity<List<UsuarioResponse>> findAll(){
		return ResponseEntity.status(HttpStatus.OK).body(service.findAll());
	}
	
	@GetMapping("/get/{id}")
	public ResponseEntity<UsuarioResponse> findById(@PathVariable Long id){
		return ResponseEntity.status(HttpStatus.OK).body(service.findById(id));
	} 
	
	@PostMapping("/create")
	public ResponseEntity<Long> createUsuario(@RequestBody @Valid UsuarioRequest request){
		Long usuarioId = service.createUsuario(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(usuarioId);
	}
	
	@PutMapping("/update/{id}")
	public ResponseEntity<Void> updateUsuario(@PathVariable Long id, @RequestBody @Valid UsuarioRequest request){
		service.updateUsuario(id, request);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> deleteUsuario(@PathVariable Long id){
		service.deleteUsuario(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	
}
