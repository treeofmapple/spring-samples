package com.tom.first.vehicle.service;

import org.springframework.stereotype.Component;

import com.tom.first.vehicle.model.Brand;
import com.tom.first.vehicle.model.Model;
import com.tom.first.vehicle.repository.ModelRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ModelUtils {

	private final ModelRepository repository;
	
	public Model findById(long query) {
		return repository.findById(query).orElseThrow(() ->{
			throw new RuntimeException("");
		});
	}
	
	public Model findOrCreate(String query, Brand brand) {
		return repository.findByNameAndBrand(query, brand).orElseGet(() -> {
			return repository.save(new Model(null, query, brand));
		});
	}
	
	public void ensureAreModelUnique(String model) {
		if (repository.existsByName(model)) {
			throw new RuntimeException("");
		}
	}

	public Model checkIfModelIsTaken(Model currentModel, Brand brand, String model) {
		if(currentModel.getName().equalsIgnoreCase(model)){
			return currentModel;
		}
		return findOrCreate(model, brand);
	}

}
