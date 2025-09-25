package com.tom.service.datagen.common;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Component;

@Component
public class IdGenerator {

	private final AtomicLong counter = new AtomicLong(1);
	
	public long nextId() {
		return counter.getAndIncrement();
	}
	
	public String generateRandomUUID() {
		return UUID.randomUUID().toString();
	}
}
