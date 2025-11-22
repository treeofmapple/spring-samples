package com.tom.first.vehicle.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tom.first.vehicle.dto.VehicleRequest;
import com.tom.first.vehicle.dto.VehicleResponse;
import com.tom.first.vehicle.service.VehicleService;

import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequestMapping("/v1/vehicle")
@RequiredArgsConstructor
public class VehicleController {

	private final VehicleService service;

	@GetMapping(params = "id")
	public ResponseEntity<VehicleResponse> findVehicleById(@RequestParam("id") long query) {
		var response = service.findById(query);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@GetMapping(params = "plate")
	public ResponseEntity<VehicleResponse> findVehicleByPlate(@RequestParam("plate") String query) {
		var response = service.findByPlate(query);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@PostMapping
	public ResponseEntity<VehicleResponse> createVehicle(@RequestBody(required = true) VehicleRequest request) {
		var response = service.createVehicle(request);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
	}

	@DeleteMapping(value = "/{plate}")
	public ResponseEntity<Void> deleteVehicle(
			// @Pattern(regexp = "[A-Z]{3}-\\d{4}", message = "Invalid plate format")
			@PathVariable String plate) {
		service.deleteVehicleByPlate(plate);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

}
