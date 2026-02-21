package com.tom.benchmark.client.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.tom.benchmark.client.dto.ClientRequest;
import com.tom.benchmark.client.dto.ClientResponse;
import com.tom.benchmark.client.mapper.ClientMapper;
import com.tom.benchmark.client.model.Client;
import com.tom.benchmark.client.repository.ClientRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClientService {

	private final ClientRepository repository;
	private final ClientMapper mapper;
	
	public List<ClientResponse> findAll(){
		List<Client> client = repository.findAll();
		if(client.isEmpty()) {
			throw new RuntimeException("No client found in the database");
		}
		return client.stream().map(mapper::toResponse).collect(Collectors.toList());
	}
	
	public ClientResponse findById(Long id) {
		return repository.findById(id).map(mapper::toResponse).orElseThrow(() -> {
			return new RuntimeException("Client not found");
		});
	}
	
	public ClientResponse findByCpf(String name) {
		return repository.findByCpf(name).map(mapper::toResponse).orElseThrow(() -> {
			return new RuntimeException("Client not found");
		});
	}

	@Transactional
	public ClientResponse createClient(ClientRequest request) {
		if(repository.existsByCpf(request.cpf())) {
			throw new RuntimeException("User with existent cpfs");
		}
		
		var client = repository.save(mapper.toClient(request));
		var response = mapper.toResponse(client);
		return response;
	}
	
	@Transactional
	public void deleteClient(String cpf) {
		if(!repository.existsByCpf(cpf)) {
			throw new RuntimeException("Client not found");
		}
		repository.deleteByCpf(cpf);
	}
	
}
