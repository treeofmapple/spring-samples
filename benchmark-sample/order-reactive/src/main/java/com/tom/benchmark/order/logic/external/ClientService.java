package com.tom.benchmark.order.logic.external;

import java.util.UUID;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

import com.tom.benchmark.order.logic.client.ClientResponse;

import reactor.core.publisher.Mono;

public interface ClientService {

	@GetExchange(value = "/v1/client/{id}")
	Mono<ClientResponse> findById(@PathVariable(value = "id") UUID clientId);

	@GetExchange(value = "/v1/client")
	Mono<ClientResponse> findByCpf(@RequestParam(value = "cpf") String dataCpf);
	
}
