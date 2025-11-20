package com.tom.rabbitmq.realtime.dto;

public record VehicleTelemetryResponse(

		Long id,
		String plateNumber,
		String model,
		Double latitude,
		Double longitude,
		Double speedKmH,
		Integer fuelPercent,
		Double engineTemperature,
		Double odometerKm

		) {

}
