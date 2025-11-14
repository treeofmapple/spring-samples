package com.tom.kafka.producer.produce;

import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.tom.kafka.producer.book.Book;
import com.tom.kafka.producer.logic.GenerateData;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class BookStreamProducer {

	@Value("${spring.kafka.producer.topic.name}")
	private String topicName;

	private final AtomicBoolean running = new AtomicBoolean(false);
	private final ThreadPoolTaskExecutor executor;
	private final BookProducer producer;
	private final GenerateData genData;

	public void startStreaming(int speed) {
		if (running.compareAndSet(false, true)) {
			executor.submit(() -> sendLoop(speed));
			log.info("Sending Book Data");
		} else {
			log.warn("Stopped Sending Book Data");
		}
	}

	public void stopStreaming() {
		running.set(false);
		log.warn("Stopped Sending Book Data");
	}

	private void sendLoop(int speed) {
		while (running.get()) {
			Book book = genData.processGenerateAnBook();
			producer.sendBook(book);
			try {
				Thread.sleep(speed > 0 ? speed : 100);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}

}
