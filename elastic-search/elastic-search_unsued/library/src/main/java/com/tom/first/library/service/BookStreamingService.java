package com.tom.first.library.service;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.tom.first.library.logic.GenerateData;
import com.tom.first.library.processes.events.BookCreatedEvent;
import com.tom.first.library.processes.events.BookListCreatedEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@Log4j2
@Service
@RequiredArgsConstructor
public class BookStreamingService {

	// Recomends to set it lower or the same as the db pool size
	@Value("${Webflux.thread.limit:4}")
	private int threadLimit;

	private final ApplicationEventPublisher eventPublisher;
	private final GenerateData genData;
	private Disposable creationSubscription;
	private Disposable creationAuthorSubscription;
	
	public void startCreatingBook(Integer speed) {
		if (creationSubscription != null && !creationSubscription.isDisposed()) {
			log.warn("Stream already running");
			return;
		}

		long interval = (speed != null && speed > 0) ? speed : 20;
		
		creationSubscription = 
				Flux.interval(Duration.ofMillis(interval))
				.onBackpressureDrop()
                .publishOn(Schedulers.boundedElastic(), threadLimit)
                .map(tick -> genData.processBook())
                .doOnNext(tick -> eventPublisher.publishEvent(new BookCreatedEvent(tick)))
                .doOnNext(book -> {
                		if (interval >= 50) {
                			log.info("Created Book {}", book.getTitle());
                		}})
                .doOnError(err -> log.error("Error while generating Books", err))
                .subscribe();
		log.info("Reactive Book streaming started with speed {} ms", speed);
	}

	public void startCreatingTenBooksOneAuthor(Integer speed) {
		if (creationAuthorSubscription != null && !creationAuthorSubscription.isDisposed()) {
			log.warn("Stream already running");
			return;
		}

		long interval = (speed != null && speed > 0) ? speed : 20;
		
		creationAuthorSubscription = 
				Flux.interval(Duration.ofMillis(interval))
                .publishOn(Schedulers.boundedElastic(), threadLimit)
                .map(tick -> genData.processBooks())
                .doOnNext(tick -> eventPublisher.publishEvent(new BookListCreatedEvent(tick)))
                .doOnNext(book -> {
                		if (interval >= 50) {
                			log.info("Created Books from Author {}", book.getFirst().getAuthor());
                		}})
                .doOnError(err -> log.error("Error while generating Books from Author", err))
                .subscribe();
		log.info("Reactive Book Author streaming started with speed {} ms", speed);
	}
	
	public void stopCreatingBook() {
		if (creationSubscription != null) {
			creationSubscription.dispose();
			creationSubscription = null;
			log.warn("Reactive Book Author streaming stopped");
		}
	}
	
	public void stopCreatingTenBooksBook() {
		if (creationAuthorSubscription != null) {
			creationAuthorSubscription.dispose();
			creationAuthorSubscription = null;
			log.warn("Reactive Book Author streaming stopped");
		}
	}
	
}
