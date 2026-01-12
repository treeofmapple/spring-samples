package com.tom.rabbitmq.realtime.processes.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.tom.rabbitmq.realtime.config.RabbitMQTopics;
import com.tom.rabbitmq.realtime.mapper.VehicleMapper;
import com.tom.rabbitmq.realtime.model.Vehicle;
import com.tom.rabbitmq.realtime.processes.events.VehicleTelemetryMessage;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class VehicleProducer {

	private final RabbitTemplate rabbitTemplate;
	private final VehicleMapper mapper;
	
    public void sendVehicleTelemetry(Vehicle vehicle) {
        VehicleTelemetryMessage messaging = mapper.buildTelemetry(vehicle);

        rabbitTemplate.convertAndSend(
                RabbitMQTopics.VEHICLE_TELEMETRY_EXCHANGE,
                RabbitMQTopics.VEHICLE_TELEMETRY_DEFAULT_KEY,
                messaging
        );

        log.info("Sent telemetry for vehicle {}", vehicle.getPlateNumber());
    }
	
}
