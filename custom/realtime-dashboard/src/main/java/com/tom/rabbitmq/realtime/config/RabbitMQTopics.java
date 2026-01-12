package com.tom.rabbitmq.realtime.config;

import org.springframework.stereotype.Component;

@Component
public class RabbitMQTopics {

	public static final String VEHICLE_TELEMETRY_QUEUE = "vehicle.telemetry.queue";
	public static final String VEHICLE_TELEMETRY_EXCHANGE = "vehicle.telemetry.exchange";
	public static final String VEHICLE_TELEMETRY_DEFAULT_KEY = "vehicle.telemetry.speed";
	
}
