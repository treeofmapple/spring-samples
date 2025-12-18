package com.tom.vehicle.webflux.service;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tom.vehicle.webflux.logic.GenerateData;

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

	private final GenerateData genData;
	private Disposable creationSubscription;
	
	public void startCreatingVehicles(Integer speed) {
		if (creationSubscription != null && !creationSubscription.isDisposed()) {
			log.warn("Stream already running");
			return;
		}

		long interval = (speed != null && speed > 0) ? speed : 20;
		
		creationSubscription = 
				Flux.interval(Duration.ofMillis(interval))
				.onBackpressureDrop()
                .publishOn(Schedulers.boundedElastic(), threadLimit)
                .map(tick -> genData.processVehicle())
                .doOnNext(vehicle -> {
                		if (interval >= 50) {
                			log.info("Created Vehicle {}", vehicle.getPlate());
                		}})
                .doOnError(err -> log.error("Error while generating vehicles", err))
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
