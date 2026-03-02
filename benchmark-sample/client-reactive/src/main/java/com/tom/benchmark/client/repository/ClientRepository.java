package com.tom.benchmark.client.repository;

import java.util.UUID;

import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.tom.benchmark.client.model.Client;

import reactor.core.publisher.Mono;

@Repository
public interface ClientRepository extends ReactiveCrudRepository<Client, UUID>, ReactiveQueryByExampleExecutor<Client> {

	Mono<Client> findByName(String name);

}
