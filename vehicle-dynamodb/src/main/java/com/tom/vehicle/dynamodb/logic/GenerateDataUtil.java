package com.tom.vehicle.dynamodb.logic;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;

import org.springframework.stereotype.Component;

import net.datafaker.Faker;

@Component
public class GenerateDataUtil {

	private final AtomicLong atomicCounter = new AtomicLong(1);
	private final ThreadLocalRandom loc = ThreadLocalRandom.current();
	private final ThreadLocal<Faker> faker = ThreadLocal.withInitial(Faker::new);

	private String generate(Supplier<String> fakerSupplier) {
		return fakerSupplier.get() + "-" + atomicCounter.getAndIncrement();
	}

	protected String generateRandomName() {
		return generate(() -> faker.get().name().name());
	}

	protected String generateIsbn() {
		return generate(() -> faker.get().code().isbn13());
	}

	protected String generateCarLicensePlate() {
		return generate(() -> faker.get().vehicle().licensePlate());
	}

	protected String generateCarModel() {
		return generate(() -> faker.get().vehicle().model());
	}

	protected String generateCarColor() {
		return generate(() -> faker.get().vehicle().color());
	}

	protected String generateCarBrand() {
		return generate(() -> faker.get().brand().car());
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

}
