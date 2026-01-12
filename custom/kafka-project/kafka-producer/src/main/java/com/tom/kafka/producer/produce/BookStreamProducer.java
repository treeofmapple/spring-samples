package com.tom.kafka.producer.produce;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tom.kafka.producer.book.Book;
import com.tom.kafka.producer.logic.GenerateData;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Log4j2
@Service
@RequiredArgsConstructor
public class BookStreamProducer {

	@Value("${spring.kafka.producer.topic.name}")
	private String topicName;

	private final BookProducer producer;
	private final GenerateData genData;
	
    private Disposable subscription;

    public void startStreaming(int speed) {
        if (subscription != null && !subscription.isDisposed()) {
            log.warn("Stream already running");
            return;
        }
        
        subscription = Flux.interval(Duration.ofMillis(speed))
                .flatMap(tick -> Mono.fromRunnable(() -> {
                    Book book = genData.processGenerateAnBook();
                    producer.sendBook(book);
                    log.info("Sent book: {}", book.getTitle());
                }))
                .subscribe();

        log.info("Reactive book streaming started with speed {} ms", speed);
        
    }

    public void stopStreaming() {
        if (subscription != null) {
            subscription.dispose();
            subscription = null;
            log.warn("Reactive book streaming stopped");
        }
    }
    
}
