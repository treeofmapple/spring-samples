package com.tom.service.datagen.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.tom.service.datagen.common.IdGenerator;
import com.tom.service.datagen.common.Operations;
import com.tom.service.datagen.exception.InternalException;
import com.tom.service.datagen.model.Employee;
import com.tom.service.datagen.service.interfaces.BatchCallback;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class EmployeeService {

	private static final String CSV_HEADER = String.join(",", "ID", "First Name", "Last Name", "Email", "Phone", "Age",
			"Gender", "Department", "Job Title", "Salary", "Experience", "Address", "Hire Date", "Termination Date");

	private final Map<String, byte[]> csvStorage = new ConcurrentHashMap<>();

	private final Operations operations;
	private final IdGenerator generator;
	private final EmployeeUtils employeeUtils;

	public byte[] generateEmployeeData(int quantity) {
		log.info("Started to generate: {} employees", quantity);
		try {

			clearPreviousData();

			List<Employee> allEmployees = generateEmployees(quantity, (batch, batchNum, totalBatches) -> {
				log.info("Batch {} of {} saved (Size: {})", batchNum, totalBatches, batch.size());
			});

			byte[] csvData = operations.convertToCSV(allEmployees, CSV_HEADER);

			log.info("Finished generating {} employees", quantity);
			return csvData;
		} catch (Exception e) {
			log.error("Error generating employee data", e);
			throw new InternalException("System internal Error");
		}
	}

	private List<Employee> generateEmployees(int quantity, BatchCallback callback) {
		final int batchSize = employeeUtils.getBatchSize();
		int totalBatches = (int) Math.ceil((double) quantity / batchSize);
		List<Employee> allEmployees = new ArrayList<>();

		for (int i = 0; i < totalBatches; i++) {
			List<Employee> batch = new ArrayList<>(batchSize);

			for (int j = 0; j < batchSize && (i * batchSize + j) < quantity; j++) {
				Employee emp = employeeUtils.generateSingleEmployee();
				batch.add(emp);
			}
			if (!batch.isEmpty()) {
				operations.logProgress(i * batchSize + batch.size(), quantity);
			}

			callback.onBatchComplete(batch, i + 1, totalBatches);
			allEmployees.addAll(batch);

			byte[] csvData = operations.convertToCSV(batch, CSV_HEADER);
			String batchId = generator.generateRandomUUID();
			csvStorage.put(batchId, csvData);
		}

		return allEmployees;
	}

	public byte[] retrieveCsvFromTempStorage(String fileId) {
		log.info("Attempting to download data for fileId: {}", fileId);
		byte[] csvData = csvStorage.get(fileId);
		if (csvData != null) {
			log.info("Successful data retrieval for fileId: {}", fileId);
		} else {
			log.warn("Data not found or expired for fileId: {}", fileId);
		}
		return csvData;
	}

	public byte[] deleteCsvFromTempStorage(String fileId) {
		log.info("Deleting Data from Storage");
		byte[] removedData = csvStorage.remove(fileId);
		if (removedData != null) {
			log.info("Successful data deletion from Storage");
		} else {
			log.warn("No data found for deletion with fileId: {}", fileId);
		}
		return removedData;
	}

	private void clearPreviousData() {
		if (!csvStorage.isEmpty()) {
			csvStorage.clear();
		}
	}

}
