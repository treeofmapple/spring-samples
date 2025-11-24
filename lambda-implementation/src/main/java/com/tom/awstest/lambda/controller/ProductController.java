package com.tom.awstest.lambda.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tom.awstest.lambda.dto.PageProductResponse;
import com.tom.awstest.lambda.dto.ProductRequest;
import com.tom.awstest.lambda.dto.ProductResponse;
import com.tom.awstest.lambda.dto.UpdateRequest;
import com.tom.awstest.lambda.service.ProductService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequestMapping("/v1/product")
@RequiredArgsConstructor
public class ProductController {

	private final ProductService service;

	@GetMapping(params = "id")
	public ResponseEntity<ProductResponse> findProductById(@RequestParam("id") @Min(0) long query) {
		var response = service.searchProductById(query);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@GetMapping
	public ResponseEntity<PageProductResponse> findProductByName(@RequestParam(defaultValue = "0") @Min(0) int page,
			@RequestParam(required = false, value = "name") String query) {
		var response = service.searchProductName(page, query);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@PostMapping
	public ResponseEntity<ProductResponse> createProduct(@RequestBody @Valid ProductRequest request) {
		var response = service.createProduct(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PutMapping(value = "/{id}")
	public ResponseEntity<ProductResponse> updateProduct(@PathVariable(value = "id") long query,
			@RequestBody @Valid UpdateRequest request) {
		var response = service.updateProduct(query, request);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> deleteProductById(@PathVariable("id") long query) {
		service.deleteProductById(query);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@PutMapping("/activate/{id}")
	public ResponseEntity<Void> activateProduct(@PathVariable("id") long query) {
		service.setProduteState(query);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

}
