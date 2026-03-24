package com.tom.aws.awstest.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tom.aws.awstest.product.ProductService;
import com.tom.aws.awstest.product.dto.PageProductResponse;
import com.tom.aws.awstest.product.dto.ProductRequest;
import com.tom.aws.awstest.product.dto.ProductResponse;
import com.tom.aws.awstest.product.dto.UpdateRequest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/product")
@RequiredArgsConstructor
public class ProductController {

	private final ProductService service;

	@GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PageProductResponse> findProductByParams(@RequestParam(defaultValue = "0") int page,
			@RequestParam(required = false) String query) {
		var response = service.searchProductByParams(page, query);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@GetMapping(value = "/search/id", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ProductResponse> findProductById(@RequestParam("id") long query) {
		var response = service.searchProductById(query);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ProductResponse> createProduct(@RequestBody @Valid ProductRequest request) {
		var response = service.createProduct(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PutMapping(value = "/edit/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ProductResponse> updateProduct(@PathVariable(value = "id") long query,
			@RequestBody @Valid UpdateRequest request) {
		var response = service.updateProduct(query, request);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
	}

	@DeleteMapping(value = "/delete/{id}")
	public ResponseEntity<Void> deleteProduct(@PathVariable("id") long query) {
		service.deleteProduct(query);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@PutMapping("/activate/{id}")
	public ResponseEntity<Void> activateProduct(@PathVariable("id") long query) {
		service.setProduteState(query);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@PostMapping("/data/generate")
	public ResponseEntity<PageProductResponse> generateProducts(@RequestParam @Min(1) int quantity) {
		var response = service.generateProducts(quantity);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
	}

}
