package com.tom.front.full.service;

import java.security.SecureRandom;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.tom.front.full.common.SecurityUtils;
import com.tom.front.full.model.Employee;
import com.tom.front.full.repository.EmployeeRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
@RequiredArgsConstructor
public class EmployeeUtils {

	@Value("${application.employee.max-retries:10}")
	private int MAX_RETRIES;

	@Value("${application.employee.code.length:8}")
	private int CODE_LENGTH;

	private final EmployeeRepository repository;
	private final SecurityUtils securityUtils;

	public Employee findById(long query) {
		return repository.findById(query).orElseThrow(() -> {
			throw new RuntimeException("");
		});
	}

	public void ensureNameAreUnique(String name) {
		if (repository.existsByName(name)) {
			throw new RuntimeException("Already Exists");
		}
	}

	public void checkIfNameIsTaken(Employee currentEmployee, String name) {
		if (!currentEmployee.getName().equalsIgnoreCase(name)) {
			if (repository.existsByNameAndIdNot(name, currentEmployee.getId())) {
				throw new RuntimeException("");
			}
		}
	}

	public void logSearchParams(String name, String email, String jobTitle, String employeeCode) {
		Map<String, Object> allFilters = new LinkedHashMap<>();
		allFilters.put("name", name);
		allFilters.put("email", email);
		allFilters.put("jobTitle", jobTitle);
		allFilters.put("employeeCode", employeeCode);

		Map<String, Object> searchParams = allFilters.entrySet().stream()
				.filter(entry -> entry.getValue() instanceof String && !((String) entry.getValue()).isBlank())
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
		if (!searchParams.isEmpty()) {
			log.info("IP: {}, searching with params: {}", securityUtils.getRequestingClientIp(), searchParams);
		}
	}

	public String generateEmployeeCode() {
		final String allowedCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		final SecureRandom random = new SecureRandom();

		for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
			String code = random.ints(CODE_LENGTH, 0, allowedCharacters.length()).mapToObj(allowedCharacters::charAt)
					.map(Object::toString).collect(Collectors.joining());

			if (!repository.existsByEmployeeCode(code)) {
				return code;
			}
		}
		throw new RuntimeException("Failed to generate a unique employee code after " + MAX_RETRIES + " attempts");

	}

}
