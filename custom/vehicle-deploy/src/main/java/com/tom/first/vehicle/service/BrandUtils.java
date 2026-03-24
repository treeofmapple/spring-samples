package com.tom.first.vehicle.service;

import org.springframework.stereotype.Component;

import com.tom.first.vehicle.model.Brand;
import com.tom.first.vehicle.repository.BrandRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BrandUtils {

	private final BrandRepository repository;
	
	public Brand findById(long query) {
		return repository.findById(query).orElseThrow(() ->{
			throw new RuntimeException("");
		});
	}
	
	public Brand findOrCreate(String query) {
		return repository.findByName(query).orElseGet(() -> {
			return repository.save(new Brand(null, query));
		});
	}
	
	public void ensureAreBrandUnique(String brand) {
		if (repository.existsByName(brand)) {
			throw new RuntimeException("");
		}
	}

	public Brand checkIfBrandIsTaken(Brand currentBrand, String brand) {
		if(currentBrand.getName().equalsIgnoreCase(brand)){
			return currentBrand;
		}
		return findOrCreate(brand);
	}
	
}
