package com.tom.aws.awstest.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.tom.aws.awstest.exception.NotFoundException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductUtil {

	private final ProductRepository repository;
	
	public Product findById(long query) {
        return repository.findById(query).orElseThrow(
        		() -> new NotFoundException("Product not found: " + query));
	}
	
	public Product findByName(String query) {
        return repository.findByName(query).orElseThrow(
        		() -> new NotFoundException("Product not found: " + query));
	}
	
	public Page<Product> findByIdOrNamePageable(String query, Pageable pageable) {
    	if(query == null || query.isBlank()) {
    		throw new NotFoundException("Product not found: " + query);
    	}
    	try {
    		Long userId = Long.parseLong(query);
    		return repository.findById(userId, pageable);
    	} catch(NotFoundException e) {
    		return repository.findByName(query, pageable);
		}
	}
	
	public void ensureUniqueName(String query) {
		if(repository.existsByName(query)) {
			throw new NotFoundException("Product already exists: " + query);
		}
	}
	
	public void checkIfNameByTakenByAnother(Product currentName, String newName) {
		if(repository.existsByNameAndIdNot(newName, currentName.getId())) {
			throw new NotFoundException("The name is already taken: " + newName);
		}
	}
	
}
