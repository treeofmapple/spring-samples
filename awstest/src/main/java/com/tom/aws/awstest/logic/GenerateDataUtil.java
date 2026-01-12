package com.tom.aws.awstest.logic;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

import com.tom.aws.awstest.image.dto.ImageGenResponse;

import net.datafaker.Faker;

@Component
public class GenerateDataUtil {

	private List<String> contentTypes = Stream.of("image/jpeg", "image/png", "image/gif", "image/webp")
			.collect(Collectors.toCollection(ArrayList::new));

	private final List<String> extensions = List.of("jpg", "png", "gif", "webp");

	private final AtomicLong atomicCounter = new AtomicLong(1);
	private final ThreadLocalRandom loc = ThreadLocalRandom.current();
	private static final ThreadLocal<Faker> faker = ThreadLocal.withInitial(Faker::new);

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

	protected ImageGenResponse generateAndDownloadImage() {
		int randomIndex = loc.nextInt(contentTypes.size());
		String contentType = contentTypes.get(randomIndex);
		String extension = extensions.get(randomIndex);
		String fileName = UUID.randomUUID().toString() + "." + extension;

		int width = getRandomInt(300, 1600);
		int height = getRandomInt(300, 1600);

		String imageUrlString = String.format("https://via.placeholder.com/%dx%d.%s", width, height, extension);

		try {
			@SuppressWarnings("deprecation")
			URL url = new URL(imageUrlString);
			try (InputStream in = url.openStream(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {

				byte[] buffer = new byte[2048];
				int bytesRead;
				while ((bytesRead = in.read(buffer)) != -1) {
					out.write(buffer, 0, bytesRead);
				}
				return new ImageGenResponse(out.toByteArray(), fileName, contentType);
			}
		} catch (IOException e) {
			throw new RuntimeException("Failed to download image from URL: " + imageUrlString, e);
		}
	}

	private String generate(Supplier<String> fakerSupplier) {
		return fakerSupplier.get() + "-" + atomicCounter.getAndIncrement();
	}
}
