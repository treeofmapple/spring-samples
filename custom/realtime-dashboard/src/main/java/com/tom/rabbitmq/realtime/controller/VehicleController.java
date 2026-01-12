package com.tom.rabbitmq.realtime.controller;

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

import com.tom.rabbitmq.realtime.dto.VehicleRequest;
import com.tom.rabbitmq.realtime.dto.VehicleResponse;
import com.tom.rabbitmq.realtime.service.VehicleService;

import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequestMapping("/v1/vehicle")
@RequiredArgsConstructor
public class VehicleController {

	private final VehicleService service;

	@GetMapping(params = "id")
	public ResponseEntity<VehicleResponse> findById(@RequestParam("id") long query) {
		var response = service.findById(query);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@GetMapping(params = "plate")
	public ResponseEntity<VehicleResponse> findByTitle(@RequestParam("plate") String query) {
		var response = service.findByPlateNumber(query);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@PostMapping
	public ResponseEntity<VehicleResponse> createVehicle(@RequestBody(required = true) VehicleRequest request) {
		var response = service.createVehicle(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@DeleteMapping(value = "/{query}")
	public ResponseEntity<Void> deleteBookByTitle(@PathVariable long query) {
		service.deleteVehicleById(query);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@DeleteMapping(params = "plate")
	public ResponseEntity<Void> deleteBookByTitle(@RequestParam String plate) {
		service.deleteVehicleByPlateNumber(plate);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

}
