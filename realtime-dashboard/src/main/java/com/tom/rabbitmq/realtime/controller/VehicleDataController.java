package com.tom.rabbitmq.realtime.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tom.rabbitmq.realtime.service.VehicleDataStreaming;

import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequestMapping("/v1/vehicle/streaming")
@RequiredArgsConstructor
public class VehicleDataController {

	private final VehicleDataStreaming dataStreaming;

	@PostMapping(value = "/telemetry/start")
	public ResponseEntity<Void> telemetryStreamStart(
			@RequestParam(required = false) long id,
			@RequestParam(value = "plate", required = false) String plateNumber,
			@RequestParam(required = false, defaultValue = "200") int speed) {
		dataStreaming.startSendingTelemetryData(id, plateNumber, speed);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@PostMapping(value = "/telemetry/stop")
	public ResponseEntity<Void> telemetryStreamStop() {
		dataStreaming.stopSendingTelemetryData();
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	@PostMapping(value = "/start")
	public ResponseEntity<Void> continuousDataCreationStart(@RequestParam(required = false, defaultValue = "200") int speed) {
		dataStreaming.startCreatingVehicles(speed);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@PostMapping(value = "/stop")
	public ResponseEntity<Void> continuousDataCreationStop() {
		dataStreaming.stopCreatingVehicles();
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
}
