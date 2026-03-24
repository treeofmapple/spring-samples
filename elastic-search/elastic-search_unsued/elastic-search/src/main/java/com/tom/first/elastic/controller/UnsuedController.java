package com.tom.first.elastic.controller;

public class UnsuedController {

	/*
	
	@GetMapping(params = "date-range")
	public ResponseEntity<List<VehicleResponse>> findVehicleCreatedBetweenRange(@RequestParam LocalDate from,
			@RequestParam LocalDate toDate) {

		ZonedDateTime start = from.atStartOfDay(ZoneId.systemDefault());
		ZonedDateTime end = toDate.plusDays(1).atStartOfDay(ZoneId.systemDefault()).minusNanos(1);

		var response = service.findByCreatedAtRange(start, end);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	 */
	
}
