package com.tom.first.elastic.controller;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tom.first.elastic.dto.VehicleResponse;
import com.tom.first.elastic.service.VehicleService;

import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequestMapping("/v1/vehicle/search")
@RequiredArgsConstructor
public class VehicleController {

	private final VehicleService service;

	@GetMapping(params = "plate")
	public ResponseEntity<VehicleResponse> findVehicleByPlate(@RequestParam String plate) {
		var response = service.findByPlate(plate);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@GetMapping(params = "brand")
	public ResponseEntity<List<VehicleResponse>> findVehicleByBrand(@RequestParam String brand) {
		var response = service.findByBrand(brand);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@GetMapping(params = "model")
	public ResponseEntity<List<VehicleResponse>> findVehicleByModel(@RequestParam String model) {
		var response = service.findByModel(model);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@GetMapping(params = "color")
	public ResponseEntity<List<VehicleResponse>> findVehicleByColor(@RequestParam String color) {
		var response = service.findByColor(color);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@GetMapping(params = "date-range")
	public ResponseEntity<List<VehicleResponse>> findVehicleCreatedBetweenRange(@RequestParam LocalDate from,
			@RequestParam LocalDate toDate) {

		ZonedDateTime start = from.atStartOfDay(ZoneId.systemDefault());
		ZonedDateTime end = toDate.plusDays(1).atStartOfDay(ZoneId.systemDefault()).minusNanos(1);

		var response = service.findByCreatedAtRange(start, end);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

}
