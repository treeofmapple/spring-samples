package com.tom.rabbitmq.realtime.service;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tom.rabbitmq.realtime.logic.GenerateData;
import com.tom.rabbitmq.realtime.model.Vehicle;
import com.tom.rabbitmq.realtime.processes.producer.VehicleProducer;
import com.tom.rabbitmq.realtime.repository.VehicleRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@Log4j2
@Service
@RequiredArgsConstructor
public class VehicleDataStreaming {

	// Recomends to set it lower or the same as the db pool size
	@Value("${Webflux.thread.limit:4}")
	private int threadLimit;

	private final VehicleRepository vehicleRepository;

	private Disposable telemetrySubscription;
	private Disposable creationSubscription;

	private final VehicleProducer producer;
	private final GenerateData genData;

	public void startSendingTelemetryData(Long id, String plateNumber, Integer speed) {
        if (telemetrySubscription != null && !telemetrySubscription.isDisposed()) {
            log.warn("Stream already running");
            return;
        }
        
        telemetrySubscription =
        		Flux.interval(Duration.ofMillis((speed != null && speed > 0) ? speed : 20))
        		.publishOn(Schedulers.boundedElastic(), threadLimit)
        		.map(tick -> findVehicle(id, plateNumber))
        		.map(genData::updatePosition)
        		.doOnNext(vehicleRepository::save)
        		.doOnNext(producer::sendVehicleTelemetry)
				.doOnNext(v -> log.info("Sent book: {}", v.getPlateNumber()))
				.subscribe();

        log.info("Reactive book streaming started with speed {} ms", speed);
    }

    private Vehicle findVehicle(Long query, String plateNumber) {
        return vehicleRepository.findById(query)
                .or(() -> vehicleRepository.findByPlateNumber(plateNumber))
                .orElseGet(() -> genData.processVehicle());
    }
	
	public void stopSendingTelemetryData() {
		if (telemetrySubscription != null) {
			telemetrySubscription.dispose();
			telemetrySubscription = null;
			log.warn("Reactive Vehicle streaming stopped");
		}
	}

	public void startCreatingVehicles(Integer speed) {
		if (creationSubscription != null && !creationSubscription.isDisposed()) {
			log.warn("Stream already running");
			return;
		}

		creationSubscription = 
				Flux.interval(Duration.ofMillis((speed != null && speed > 0) ? speed : 20))
                .publishOn(Schedulers.boundedElastic(), threadLimit)
                .map(tick -> genData.processVehicle())
                .doOnNext(v -> log.info("Created Vehicle {}", v.getPlateNumber()))
                .subscribe();

		log.info("Reactive Vehicle streaming started with speed {} ms", speed);
	}

	public void stopCreatingVehicles() {
		if (creationSubscription != null) {
			creationSubscription.dispose();
			creationSubscription = null;
			log.warn("Reactive Vehicle streaming stopped");
		}
	}

}
