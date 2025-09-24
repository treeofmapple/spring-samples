package com.tom.service.datagen.service;

import java.time.LocalDate;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Component;

import com.tom.service.datagen.common.GenerateData;
import com.tom.service.datagen.dto.RandomRequest;
import com.tom.service.datagen.model.Employee;
import com.tom.service.datagen.model.enums.Gender;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class EmployeeUtils extends GenerateData {

	private final Set<String> usedEmails = ConcurrentHashMap.newKeySet();
	private final Set<String> usedPhoneNumbers = ConcurrentHashMap.newKeySet();

	private int gender, age, experience, salary;

	public Employee generateSingleEmployee() {
		Employee emp = new Employee();
		emp.setId(atomicCounter.incrementAndGet());
		boolean isMale = ThreadLocalRandom.current().nextInt(100) < gender;
		emp.setGender(isMale ? Gender.MALE : Gender.FEMALE);

		do {
			emp.setFirstName(isMale ? faker.get().name().maleFirstName() : faker.get().name().femaleFirstName());

			emp.setEmail(faker.get().internet().safeEmailAddress());
			emp.setPhoneNumber(faker.get().phoneNumber().cellPhone());
		} while (usedEmails.contains(emp.getEmail()) || usedPhoneNumbers.contains(emp.getPhoneNumber()));

		usedEmails.add(emp.getEmail());
		usedPhoneNumbers.add(emp.getPhoneNumber());

		emp.setLastName(faker.get().name().lastName());
		emp.setDepartment(faker.get().company().industry());
		emp.setJobTitle(faker.get().job().title());
		emp.setAddress(faker.get().address().fullAddress());

		emp.setAge(getRandomNumber(isAtributesMet(age) ? 19 : 41, isAtributesMet(age) ? 41 : 59));

		emp.setYearsOfExperience(
				getRandomNumber(isAtributesMet(experience) ? 1 : 11, isAtributesMet(experience) ? 11 : 31));

		emp.setSalary(
				getRandomNumber(isAtributesMet(salary) ? 30000 : 400000, isAtributesMet(salary) ? 400001 : 700001));

		LocalDate hireDate = LocalDate.now().minusDays(getRandomNumber(1, 3650));
		emp.setHireDate(hireDate);
		emp.setTerminationDate(ThreadLocalRandom.current().nextInt(100) < 20 ? hireDate.plusDays(15) : null);

		return emp;
	}

	public void setVariables(RandomRequest request) {
		log.info("Inserting values of variables || Gender: {}, Age: {}, Experience: {}, Salary: {}", request.gender(),
				request.age(), request.experience(), request.salary());
		gender = request.gender();
		age = request.age();
		experience = request.experience();
		salary = request.salary();
	}

}
