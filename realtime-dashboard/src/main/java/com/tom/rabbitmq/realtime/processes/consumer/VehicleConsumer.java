package com.tom.rabbitmq.realtime.processes.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.tom.rabbitmq.realtime.config.RabbitMQTopics;
import com.tom.rabbitmq.realtime.processes.events.VehicleTelemetryMessage;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class VehicleConsumer {

	private final SimpMessagingTemplate messagingTemplate;

	@RabbitListener(queues = RabbitMQTopics.VEHICLE_TELEMETRY_QUEUE)
	public void consume(@Payload VehicleTelemetryMessage message) {
		log.info("Received Telemetry: {}", message);
		
		messagingTemplate.convertAndSend("/topic/telemetry", message);
	}

}
