package com.tom.service.datagen.common;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tom.service.datagen.exception.InternalException;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
@RequiredArgsConstructor
public class Operations {

	private final ObjectMapper objectMapper;

	public void logProgress(int current, int total) {
		int barSize = 20;
		int progress = (int) (((double) current / total) * barSize);
		String bar = "[" + "=".repeat(progress) + " ".repeat(barSize - progress) + "]";
		log.info("Batch Progress: {} {}/{}", bar, current, total);
	}

	public <T> byte[] convertToCSV(List<T> data, String header, Function<T, String> rowMapper) {
		try (ByteArrayOutputStream out = new ByteArrayOutputStream();
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8))) {

			writer.write(header);
			writer.newLine();

			if (data != null) {
				for (T item : data) {
					writer.write(rowMapper.apply(item));
					writer.newLine();
				}
			}

			writer.flush();
			return out.toByteArray();
		} catch (IOException e) {
			log.error("Error generating CSV byte array", e);
			throw new InternalException("Error converting data to CSV");
		}
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

	public byte[] resourceToBytes(Map<String, byte[]> files) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try (ZipOutputStream zos = new ZipOutputStream(baos)) {
			for (Map.Entry<String, byte[]> fileEntry : files.entrySet()) {
				ZipEntry entry = new ZipEntry(fileEntry.getKey());
				zos.putNextEntry(entry);
				zos.write(fileEntry.getValue());
				zos.closeEntry();
				log.info("Added {} to ZIP.", fileEntry.getKey());
			}
		} catch (IOException e) {
			log.error("Failed to create zip archive.", e);
			throw new InternalException("Failed to create zip archive for user data.");
		}
		return baos.toByteArray();
	}

}
