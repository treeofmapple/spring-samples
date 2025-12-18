package com.tom.vehicle.webflux.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tom.vehicle.webflux.service.VehicleDataStreaming;

import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequestMapping("/v1/vehicle/streaming")
@RequiredArgsConstructor
public class VehicleStreamingController {

	private final VehicleDataStreaming dataStreaming;
	
	@PostMapping(value = "/start")
	public ResponseEntity<Void> startDataStreaming(@RequestParam(required = false, defaultValue = "20") int speed) {
		dataStreaming.startCreatingVehicles(speed);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@PostMapping(value = "/stop")
	public ResponseEntity<Void> stopDataStreaming() {
		dataStreaming.stopCreatingVehicles();
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
}
