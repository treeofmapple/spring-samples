package operator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

import net.datafaker.Faker;

public class GenerateData {

	private int batchSize = 10000;
	
	protected final ThreadLocal<Faker> faker = ThreadLocal.withInitial(Faker::new);
	protected ThreadLocalRandom loc = ThreadLocalRandom.current();
	
	protected String genderChance(short gender) {
		boolean isMale = ThreadLocalRandom.current().nextInt(100) < gender;
		return isMale ? generateRandomMaleName() : generateRandomFemaleName();
	}
	
	public int getRandomNumber(int min, int max) {
	    if (max <= min) {
	        return min;
	    }
	    return loc.nextInt(max - min) + min;
	}

	public boolean isAtributesMet(int atribute) {
		return loc.nextInt(100) < atribute;
	} 
	
	public String generateRandomName() {
		return generate(() -> faker.get().name().name());
	}
	
	public String generateRandomMaleName() {
		return generate(() -> faker.get().name().maleFirstName());
	}
	
	public String generateRandomFemaleName() {
		return generate(() -> faker.get().name().femaleFirstName());
	}
	
	public String generateIsbn() {
		return generate(() -> faker.get().code().isbn13());
	}

	public String generateProductName() {
		return generate(() -> faker.get().commerce().productName());
	}

	public String generateBookTitle() {
		return generate(() -> faker.get().book().title());
	}

	public String generateAuthorNames() {
		return generate(() -> faker.get().book().author());
	}

	public String generateDeviceManufacturerName() {
		return generate(() -> faker.get().device().manufacturer());
	}

	public String generateCompanyName() {
		return generate(() -> faker.get().company().name());
	}

	public int generateRandomNumber(int value) {
		return loc.nextInt(value);
	}

	public int generateRandomInt(int min, int max) {
		return loc.nextInt(min, max);
	}

	public short generateRandomShort(int min, int max) {
		return (short) loc.nextInt(min,max);
	}
	
	public Integer getRandomInteger(int min, int max) {
		return loc.nextInt(min, max);
	}

	public double generateRandomDouble(int min) {
		return loc.nextDouble(min);
	}

	public double generateRandomDouble(int min, int max) {
		return loc.nextDouble(min, max);
	}

	public double generateRandomDouble(double min) {
		return loc.nextDouble(min);
	}

	public double generateRandomDouble(double min, double max) {
		return loc.nextDouble(min, max);
	}

	public BigDecimal generateRandomBigDecimal(int min) {
		return BigDecimal.valueOf(loc.nextDouble(min));
	}

	public BigDecimal generateRandomBigDecimal(int min, int max) {
		return BigDecimal.valueOf(loc.nextDouble(min, max));
	}

	public BigDecimal generateRandomBigDecimal(double min) {
		return BigDecimal.valueOf(loc.nextDouble(min));
	}

	public BigDecimal generateRandomBigDecimal(double min, double max) {
		return BigDecimal.valueOf(loc.nextDouble(min, max));
	}

	public LocalDate getRandomDate(LocalDate minDate, LocalDate maxDate) {
		long minDay = minDate.toEpochDay();
		long maxDay = maxDate.toEpochDay();
		long randomEpochDay = loc.nextLong(minDay, maxDay);
		return LocalDate.ofEpochDay(randomEpochDay);
	}
	
	private String generate(Supplier<String> fakerSupplier) {
		return fakerSupplier.get();
	}
	
	public int getBatchSize() {
		return this.batchSize;
	} 
	
}
