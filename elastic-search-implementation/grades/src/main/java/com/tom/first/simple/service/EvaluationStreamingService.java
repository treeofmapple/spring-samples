package com.tom.first.simple.service;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.tom.first.simple.logic.GenerateData;
import com.tom.first.simple.processes.events.EvaluationCreatedEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@Log4j2
@Service
@RequiredArgsConstructor
public class EvaluationStreamingService {

	// Recomends to set it lower or the same as the db pool size
	@Value("${Webflux.thread.limit:4}")
	private int threadLimit;

	private final ApplicationEventPublisher eventPublisher;
	private final GenerateData genData;
	private Disposable creationSubscription;
	
	public void startCreatingEvaluation(Integer speed) {
		if (creationSubscription != null && !creationSubscription.isDisposed()) {
			log.warn("Stream already running");
			return;
		}

		long interval = (speed != null && speed > 0) ? speed : 20;
		
		creationSubscription = 
				Flux.interval(Duration.ofMillis(interval))
				.onBackpressureDrop()
                .publishOn(Schedulers.boundedElastic(), threadLimit)
                .map(tick -> genData.processEvaluation())
                .doOnNext(tick -> eventPublisher.publishEvent(new EvaluationCreatedEvent(tick)))
                .doOnNext(evaluation -> {
                		if (interval >= 50) {
                			log.info("Created Evaluation {}", evaluation.getSubject());
                		}})
                .doOnError(err -> log.error("Error while generating Evaluation", err))
                .subscribe();
		log.info("Reactive Evaluation streaming started with speed {} ms", speed);
	}

	public void stopCreatingEvaluation() {
		if (creationSubscription != null) {
			creationSubscription.dispose();
			creationSubscription = null;
			log.warn("Reactive Evaluation streaming stopped");
		}
	}
	
}
