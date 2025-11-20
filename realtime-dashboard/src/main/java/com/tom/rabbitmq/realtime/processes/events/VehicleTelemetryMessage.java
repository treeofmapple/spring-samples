package com.tom.rabbitmq.realtime.processes.events;

public record VehicleTelemetryMessage(

		Long id,
		Double latitude,
		Double longitude,
		Double speedKmH,
		Integer fuelPercent,
		Double odometerKm
		
		) {

}
