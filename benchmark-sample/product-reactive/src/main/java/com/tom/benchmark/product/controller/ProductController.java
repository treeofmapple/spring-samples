package com.tom.benchmark.product.controller;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Validated
@RestController
@RequestMapping("/v1/product")
@RequiredArgsConstructor
public class ProductController {

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public Mono<String> fetchComputerIp() {
		return null;
	}

}
