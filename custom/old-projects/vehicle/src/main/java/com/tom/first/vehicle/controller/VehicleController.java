package com.tom.first.vehicle.controller;

import java.util.List;

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

import com.tom.first.vehicle.request.VehicleRequest;
import com.tom.first.vehicle.request.VehicleResponse;
import com.tom.first.vehicle.request.VehicleUpdate;
import com.tom.first.vehicle.service.VehicleService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/vehicle")
@RequiredArgsConstructor
public class VehicleController {

	private final VehicleService service;

	@GetMapping("/get")
	public ResponseEntity<List<VehicleResponse>> findAll() {
		return ResponseEntity.status(HttpStatus.OK).body(service.findAll());
	}

	@GetMapping("/get/name")
	public ResponseEntity<VehicleResponse> findByName(@RequestParam String plate) {
		return ResponseEntity.status(HttpStatus.OK).body(service.findByPlate(plate));
	}

	@PostMapping("/create")
	public ResponseEntity<VehicleResponse> createVehicle(@RequestBody @Valid VehicleRequest request) {
		var establishment = service.createVehicle(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(establishment);
	}

	@PutMapping("/update")
	public ResponseEntity<VehicleResponse> updateVehicle(@RequestParam String plate,
			@RequestBody @Valid VehicleUpdate request) {
		var establishment = service.updateVehicle(plate, request);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(establishment);
	}

	@DeleteMapping("/delete")
	public ResponseEntity<String> deleteVehicle(@PathVariable String plate) {
		service.deleteVehicle(plate);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Successfull");
	}

}
