package com.tom.first.vehicle.controller;

import org.springframework.http.HttpStatus;
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

import com.tom.first.vehicle.dto.BrandResponse;
import com.tom.first.vehicle.dto.ModelResponse;
import com.tom.first.vehicle.dto.PageVehicleResponse;
import com.tom.first.vehicle.dto.VehicleRequest;
import com.tom.first.vehicle.dto.VehicleResponse;
import com.tom.first.vehicle.dto.VehicleUpdate;
import com.tom.first.vehicle.service.VehicleService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/vehicle")
@RequiredArgsConstructor
public class VehicleController {

	private final VehicleService service;

	@GetMapping(value = "/search")
	public ResponseEntity<PageVehicleResponse> searchVehicleByParams(
			@RequestParam(defaultValue = "0") @Min(0) int page,
			@RequestParam(required = false) String plate,
			@RequestParam(required = false) String brand,
			@RequestParam(required = false) String model,
			@RequestParam(required = false) String color
			) {
		var response = service.searchVehicleByParams(page, plate, brand, model, color);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<VehicleResponse> findVehicleById(
			@PathVariable("id") long query
			) {
		var response = service.findById(query);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@GetMapping(value = "/brands/{id}")
	public ResponseEntity<BrandResponse> findBrandById(
			@PathVariable("id") long query
			) {
		var response = service.findBrandById(query);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@GetMapping(value = "/models/{id}")
	public ResponseEntity<ModelResponse> findModelById(
			@PathVariable("id") long query
			) {
		var response = service.findModelById(query);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@PostMapping(value = "/create")
	public ResponseEntity<VehicleResponse> createVehicle(
			@RequestBody @Valid VehicleRequest request) {
		var response = service.createVehicle(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PutMapping(value = "/update/{id}")
	public ResponseEntity<VehicleResponse> updateVehicle(
			@PathVariable("id") long query,
			@RequestBody @Valid VehicleUpdate request) {
		var response = service.updateVehicle(query, request);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@DeleteMapping(value = "/delete/{id}")
	public ResponseEntity<Void> deleteVehicle(
			@PathVariable("id") long query)  {
		service.deleteVehicle(query);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

}
