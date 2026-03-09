package com.tom.benchmark.order.openfeign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.tom.benchmark.order.dto.ClientRequest;
import com.tom.benchmark.order.dto.ClientResponse;

import jakarta.validation.Valid;

@FeignClient(name = "client-service", url = "${feign.client-service.url}")
public interface ClientServiceFeign {

	@GetMapping(value = "/v1/client/get", produces = MediaType.APPLICATION_JSON_VALUE)
	ClientResponse findByCpf(@RequestParam String cpf);

	@PostMapping(value = "/v1/client/create", 
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	ClientResponse createClient(@RequestBody @Valid ClientRequest request);
	
	@DeleteMapping(value = "/delete")
	Void deleteClient(@RequestParam String cpf);
}
