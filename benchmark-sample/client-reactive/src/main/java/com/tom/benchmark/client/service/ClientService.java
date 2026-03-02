package com.tom.benchmark.client.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tom.benchmark.client.dto.ClientRequest;
import com.tom.benchmark.client.dto.ClientResponse;
import com.tom.benchmark.client.dto.ClientUpdate;
import com.tom.benchmark.client.dto.PageClientResponse;
import com.tom.benchmark.client.exception.sql.DataViolationException;
import com.tom.benchmark.client.exception.sql.NotFoundException;
import com.tom.benchmark.client.mapper.ClientMapper;
import com.tom.benchmark.client.model.Client;
import com.tom.benchmark.client.repository.ClientRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;

@Log4j2
@Service
@RequiredArgsConstructor
public class ClientService {

	@Value("${application.page.size:20}")
	private int PAGE_SIZE;

	private final ClientRepository repository;
	private final ClientMapper mapper;
	private final R2dbcEntityTemplate entityTemplate;

	@Transactional(readOnly = true)
	public Mono<ClientResponse> searchClientById(UUID clientId) {
		return repository.findById(clientId).map(mapper::toResponse)
				.switchIfEmpty(Mono.error(new NotFoundException("Client with ID: " + clientId + " was not found.")));
	}

	@Transactional(readOnly = true)
	public Mono<PageClientResponse> searchClientByParams(int page, String name, String cpf) {
		Client probe = Client.builder().name(name).cpf(cpf).build();

		ExampleMatcher matcher = ExampleMatcher.matchingAll().withIgnoreCase()
				.withStringMatcher(StringMatcher.CONTAINING).withIgnoreNullValues();

		return repository.findAll(Example.of(probe, matcher)).skip((long) page * PAGE_SIZE).take(PAGE_SIZE)
				.collectList().map(list -> mapper.toResponse(list, page, PAGE_SIZE));
	}

	@Transactional
	public Mono<ClientResponse> createClient(ClientRequest request) {
		return repository.findByName(request.name()).flatMap(existentUser -> {
			return Mono.<Client>error(
					new DataViolationException("Client with name: " + request.name() + " already exists."));
		}).switchIfEmpty(Mono.defer(() -> {
			Client client = mapper.build(request);
			return entityTemplate.insert(client);
		})).map(mapper::toResponse);
	}

	@Transactional
	public Mono<ClientResponse> updateClient(ClientUpdate request) {
		return repository.findById(request.userId())
				.switchIfEmpty(
						Mono.error(new NotFoundException("Client with ID: " + request.userId() + " was not found.")))
				.flatMap(existentUser -> {
					mapper.update(existentUser, request);
					existentUser.setId(request.userId());
					return entityTemplate.update(existentUser);
				}).map(mapper::toResponse);
	}

	@Transactional
	public Mono<Void> deleteClient(UUID userId) {
		return repository.existsById(userId).flatMap(exists -> {
			if (Boolean.TRUE.equals(exists)) {
				return repository.deleteById(userId);
			}
			return Mono.error(new NotFoundException("Client with ID: " + userId + " was not found."));
		});
	}

}
