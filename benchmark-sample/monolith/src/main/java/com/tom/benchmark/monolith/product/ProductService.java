package com.tom.benchmark.monolith.product;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

	private final ProductRepository repository;
	private final ProductMapper mapper;

	@Transactional(readOnly = true)
	public List<ProductResponse> findAll() {
		List<Product> products = repository.findAll();
		if(products.isEmpty()) {
			throw new RuntimeException("No products found in the database.");
		}
		return products.stream().map(mapper::toResponse).collect(Collectors.toList());
	}
	
	@Transactional(readOnly = true)
	public ProductResponse findById(Long id) {
		return repository.findById(id).map(mapper::toResponse).orElseThrow(()-> {
			return new RuntimeException("Product not found");
		});
	}
	
	@Transactional(readOnly = true)
	public ProductResponse findBySku(String sku) {
		return repository.findBySku(sku).map(mapper::toResponse).orElseThrow(()-> {
			return new RuntimeException("Product not found");
		});
	}
	
	@Transactional
	public ProductResponse createProduct(ProductRequest request) {
		if(repository.existsBySku(request.sku())) {
			throw new RuntimeException("Product with existent sku");
		}
		
		var product = repository.save(mapper.toProduct(request));
		var response = mapper.toResponse(product);
		return response;
	}
	
	@Transactional
	public void deleteProduct(String sku) {
		if(!repository.existsBySku(sku)) {
			throw new RuntimeException("Product not found");
		}
		repository.deleteBySku(sku);
	}
}
