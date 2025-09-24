package com.tom.service.datagen.common;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;
import net.datafaker.Faker;

@Log4j2
@Component
public class GenerateData {

	@Value("${application.datagen.batchSize:10000}")
	private int batchSize;
	
	protected final ThreadLocal<Faker> faker = ThreadLocal.withInitial(Faker::new);
	protected AtomicLong atomicCounter = new AtomicLong(0);
	protected ThreadLocalRandom loc = ThreadLocalRandom.current();
	
	protected int getRandomNumber(int min, int max) {
	    if (max <= min) {
	        return min;
	    }
	    return loc.nextInt(max - min) + min;
	}

	protected boolean isAtributesMet(int atribute) {
		return loc.nextInt(100) < atribute;
	} 
	
	protected String generateRandomName() {
		return generate(() -> faker.get().name().name());
	}

	protected String generateIsbn() {
		return generate(() -> faker.get().code().isbn13());
	}

	protected String generateProductName() {
		return generate(() -> faker.get().commerce().productName());
	}

	protected String generateBookTitle() {
		return generate(() -> faker.get().book().title());
	}

	protected String generateAuthorNames() {
		return generate(() -> faker.get().book().author());
	}

	protected String generateDeviceManufacturerName() {
		return generate(() -> faker.get().device().manufacturer());
	}

	protected String generateCompanyName() {
		return generate(() -> faker.get().company().name());
	}

	protected int getRandomNumber(int value) {
		return loc.nextInt(value);
	}

	protected int getRandomInt(int min, int max) {
		return loc.nextInt(min, max);
	}

	protected Integer getRandomInteger(int min, int max) {
		return loc.nextInt(min, max);
	}

	protected double getRandomDouble(int min) {
		return loc.nextDouble(min);
	}

	protected double getRandomDouble(int min, int max) {
		return loc.nextDouble(min, max);
	}

	protected double getRandomDouble(double min) {
		return loc.nextDouble(min);
	}

	protected double getRandomDouble(double min, double max) {
		return loc.nextDouble(min, max);
	}

	protected BigDecimal getRandonBigDecimal(int min) {
		return BigDecimal.valueOf(loc.nextDouble(min));
	}

	protected BigDecimal getRandonBigDecimal(int min, int max) {
		return BigDecimal.valueOf(loc.nextDouble(min, max));
	}

	protected BigDecimal getRandonBigDecimal(double min) {
		return BigDecimal.valueOf(loc.nextDouble(min));
	}

	protected BigDecimal getRandonBigDecimal(double min, double max) {
		return BigDecimal.valueOf(loc.nextDouble(min, max));
	}

	protected LocalDate getRandomDate(LocalDate minDate, LocalDate maxDate) {
		long minDay = minDate.toEpochDay();
		long maxDay = maxDate.toEpochDay();
		long randomEpochDay = loc.nextLong(minDay, maxDay);
		return LocalDate.ofEpochDay(randomEpochDay);
	}
	
	private String generate(Supplier<String> fakerSupplier) {
		return fakerSupplier.get() + "-" + atomicCounter.getAndIncrement();
	}
	
	public int getBatchSize() {
		return this.batchSize;
	} 
	
}
