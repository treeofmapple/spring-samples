package com.tom.first.username.service;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.tom.first.username.logic.GenerateData;
import com.tom.first.username.processes.events.UserCreatedEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserDataStreaming {

	// Recomends to set it lower or the same as the db pool size
	@Value("${Webflux.thread.limit:4}")
	private int threadLimit;

	private final ApplicationEventPublisher eventPublisher;
	private final GenerateData genData;
	private Disposable creationSubscription;
	
	public void startCreatingUsers(Integer speed) {
		if (creationSubscription != null && !creationSubscription.isDisposed()) {
			log.warn("Stream already running");
			return;
		}

		long interval = (speed != null && speed > 0) ? speed : 20;
		
		creationSubscription = 
				Flux.interval(Duration.ofMillis(interval))
				.onBackpressureDrop()
				.publishOn(Schedulers.boundedElastic(), threadLimit)
                .map(tick -> genData.processUser())
                .doOnNext(tick -> eventPublisher.publishEvent(new UserCreatedEvent(tick)))
                .doOnNext(user -> {
                		if (interval >= 50) {
                			log.info("Created User with email {}", user.getEmail());
                		}})
                .doOnError(err -> log.error("Error while generating users", err))
                .subscribe();
		log.info("Reactive User streaming started with speed {} ms", speed);
	}

	public void stopCreatingUsers() {
		if (creationSubscription != null) {
			creationSubscription.dispose();
			creationSubscription = null;
			log.warn("Reactive User streaming stopped");
		}
	}
	
}
