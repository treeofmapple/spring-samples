package com.tom.rabbitmq.realtime.logic;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Component;

import net.datafaker.Faker;

@Component
public class GenerateDataUtil {

	private final ThreadLocal<Faker> faker = ThreadLocal.withInitial(Faker::new);
	
	protected String generateLatitude() {
		return faker.get().address().latitude();
	}

	protected String generateLongitude() {
		return faker.get().address().longitude();
	}

	protected String generatePassword() {
		return faker.get().credentials().password(8, 12);
	}

	protected String generateEmail() {
		return faker.get().internet().emailAddress();
	}

	protected String generateUsername() {
		return faker.get().credentials().username();
	}

	protected String generateDescription() {
		return faker.get().restaurant().description();
	}

	protected String generateSubject() {
		return faker.get().internet().emailSubject();
	}

	protected String generateRandomName() {
		return faker.get().name().name();
	}

	protected String generateIsbn() {
		return faker.get().code().isbn13();
	}

	protected String generateCarLicensePlate() {
		return faker.get().vehicle().licensePlate();
	}

	protected String generateCarModel() {
		return faker.get().vehicle().model();
	}

	protected String generateCarColor() {
		return faker.get().vehicle().color();
	}

	protected String generateCarBrand() {
		return faker.get().brand().car();
	}

	protected String generateProductName() {
		return faker.get().commerce().productName();
	}

	protected String generateBookTitle() {
		return faker.get().book().title();
	}

	protected String generateAuthorNames() {
		return faker.get().book().author();
	}

	protected String generateDeviceManufacturerName() {
		return faker.get().device().manufacturer();
	}

	protected String generateCompanyName() {
		return faker.get().company().name();
	}

	protected int getRandomNumber(int value) {
		return ThreadLocalRandom.current().nextInt(value);
	}

	protected int getRandomInt(int min, int max) {
		return ThreadLocalRandom.current().nextInt(min, max);
	}

	protected Integer getRandomInteger(int min, int max) {
		return ThreadLocalRandom.current().nextInt(min, max);
	}

	protected double getRandomDouble(int min) {
		return ThreadLocalRandom.current().nextDouble(min);
	}

	protected double getRandomDouble(int min, int max) {
		return ThreadLocalRandom.current().nextDouble(min, max);
	}

	protected double getRandomDouble(double min) {
		return ThreadLocalRandom.current().nextDouble(min);
	}

	protected double getRandomDouble(double min, double max) {
		return ThreadLocalRandom.current().nextDouble(min, max);
	}

	protected BigDecimal getRandonBigDecimal(int min) {
		return BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(min));
	}

	protected BigDecimal getRandonBigDecimal(int min, int max) {
		return BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(min, max));
	}

	protected BigDecimal getRandonBigDecimal(double min) {
		return BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(min));
	}

	protected BigDecimal getRandonBigDecimal(double min, double max) {
		return BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(min, max));
	}

	protected LocalDate getRandomDate(LocalDate minDate, LocalDate maxDate) {
		long minDay = minDate.toEpochDay();
		long maxDay = maxDate.toEpochDay();
		long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay + 1);
		return LocalDate.ofEpochDay(randomDay);
	}

}
