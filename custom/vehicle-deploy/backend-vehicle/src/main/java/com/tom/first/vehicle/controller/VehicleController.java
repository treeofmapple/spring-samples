package com.tom.first.vehicle.controller;

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

import com.tom.first.vehicle.dto.BrandResponse;
import com.tom.first.vehicle.dto.ModelResponse;
import com.tom.first.vehicle.dto.PageVehicleResponse;
import com.tom.first.vehicle.dto.VehicleRequest;
import com.tom.first.vehicle.dto.VehicleResponse;
import com.tom.first.vehicle.dto.VehicleUpdate;
import com.tom.first.vehicle.service.VehicleService;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequestMapping("/v1/vehicle")
@RequiredArgsConstructor
public class VehicleController {

	private final VehicleService service;

	@GetMapping(value = "/search")
	public ResponseEntity<PageVehicleResponse> searchVehicleByParams(@RequestParam(defaultValue = "0") @Min(0) int page,
			@RequestParam(required = false) String plate, @RequestParam(required = false) String brand,
			@RequestParam(required = false) String model, @RequestParam(required = false) String color) {
		var response = service.searchVehicleByParams(page, plate, brand, model, color);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@GetMapping(params = "query")
	public ResponseEntity<VehicleResponse> findVehicleById(@RequestParam long query) {
		var response = service.findById(query);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@GetMapping(value = "/brand", params = "query")
	public ResponseEntity<BrandResponse> findBrandById(@RequestParam long query) {
		var response = service.findBrandById(query);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@GetMapping(value = "/model", params = "query")
	public ResponseEntity<ModelResponse> findModelById(@RequestParam long query) {
		var response = service.findModelById(query);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@PostMapping
	public ResponseEntity<VehicleResponse> createVehicle(@RequestBody(required = true) VehicleRequest request) {
		var response = service.createVehicle(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PutMapping(value = "/{id}")
	public ResponseEntity<VehicleResponse> updateVehicle(@PathVariable("id") long query,
			@RequestBody(required = true) VehicleUpdate request) {
		var response = service.updateVehicle(query, request);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> deleteVehicle(@PathVariable("id") long query) {
		service.deleteVehicle(query);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

}
