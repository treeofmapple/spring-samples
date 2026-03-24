package com.tom.first.elastic.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tom.first.elastic.dto.VehicleResponse;
import com.tom.first.elastic.dto.paged.PageVehicleResponse;
import com.tom.first.elastic.service.VehicleService;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequestMapping("/v1/vehicle/search")
@RequiredArgsConstructor
public class VehicleController {

	private final VehicleService service;

	@GetMapping
	public ResponseEntity<PageVehicleResponse> searchVehicleByParams(
			@RequestParam(defaultValue = "0") @Min(0) int page,
			@RequestParam(required = false) String plate, 
			@RequestParam(required = false) String brand,
			@RequestParam(required = false) String model, 
			@RequestParam(required = false) String color) {
		var response = service.searchVehicleByParams(page, plate, brand, model, color);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@GetMapping(params = "plate")
	public ResponseEntity<VehicleResponse> findVehicleByPlate(@RequestParam String plate) {
		var response = service.findByPlate(plate);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

}
