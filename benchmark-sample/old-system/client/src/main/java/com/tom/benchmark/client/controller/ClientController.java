package com.tom.benchmark.client.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tom.benchmark.client.dto.ClientRequest;
import com.tom.benchmark.client.dto.ClientResponse;
import com.tom.benchmark.client.service.ClientService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/client")
@RequiredArgsConstructor
public class ClientController {

	private final ClientService service;
	
	@GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ClientResponse>> findAll() {
		var response = service.findAll();
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ClientResponse> findById(@PathVariable Long id){
		var response = service.findById(id);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@GetMapping(value = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ClientResponse> findByCpf(@RequestParam String cpf) {
		var response = service.findByCpf(cpf);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@PostMapping(value = "/create",
				consumes = MediaType.APPLICATION_JSON_VALUE,
				produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ClientResponse> createClient(@RequestBody @Valid ClientRequest request) {
		var response = service.createClient(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}	
	
	@DeleteMapping(value = "/delete")
	public ResponseEntity<Void> deleteClient(@RequestParam String cpf) {
		service.deleteClient(cpf);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	
}
