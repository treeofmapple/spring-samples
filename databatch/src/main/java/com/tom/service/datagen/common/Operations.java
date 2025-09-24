package com.tom.service.datagen.common;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tom.service.datagen.model.Employee;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
@RequiredArgsConstructor
public class Operations {

	private final ObjectMapper objectMapper;
	
	public String generateRandomUUID() {
		return UUID.randomUUID().toString();
	}

	public void logProgress(int current, int total) {
		int barSize = 20;
		int progress = (int) (((double) current / total) * barSize);
		String bar = "[" + "=".repeat(progress) + " ".repeat(barSize - progress) + "]";
		log.info("Batch Progress: {} {}/{}", bar, current, total);
	}

	public byte[] convertToCSV(List<Employee> employees, String CSV_HEADER) {
		try (ByteArrayOutputStream out = new ByteArrayOutputStream();
				PrintWriter writer = new PrintWriter(out, true, StandardCharsets.UTF_8)) {

			writer.println(CSV_HEADER);

			for (Employee emp : employees) {
				writer.println(buildCsvRow(emp));
			}

			writer.flush();
			return out.toByteArray();
		} catch (Exception e) {
			log.error("Error generating CSV", e);
			return new byte[0];
		}
	}

	private String buildCsvRow(Employee emp) {
		StringBuilder row = new StringBuilder();

		Field[] fields = Employee.class.getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			try {
				Object value = field.get(emp);
				row.append(value).append(",");
			} catch (IllegalAccessException e) {
				log.error("Error accessing field: " + field.getName(), e);
				row.append(",");
			}
		}
		if (row.length() > 0) {
			row.setLength(row.length() - 1);
		}
		return row.toString();
	}

	public byte[] bytesToJson(Object generic) {
		try {
			return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(generic);
		} catch (JsonProcessingException e) {
			log.error("Unable to gather data", e);
			throw new IllegalStateException("Failed to process user data for export.", e);
		}
	}

	public byte[] resourceToBytes(byte[] data, String filename) {
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ZipOutputStream zos = new ZipOutputStream(baos)) {
			ZipEntry entry = new ZipEntry(filename);
			zos.putNextEntry(entry);
			zos.write(data);
			zos.closeEntry();
			return baos.toByteArray();
		} catch (IOException e) {
			log.error("Failed to create zip file", e);
			throw new IllegalStateException("Failed to create zip archive for user data.", e);
		}
	}
	
}
