package com.tom.service.datagen.common;

import org.apache.catalina.connector.ClientAbortException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class ConnectionUtils {

	public void isClientConnected(HttpServletRequest request) {
		
		
		try {
			if (!request.isAsyncStarted() && request.getInputStream().available() == 0) {
				log.warn("Client aborted the request before processing");
			}
		} catch (Exception e) {
			throw new ClientAbortException("Client disconnected during data generation");
			log.error("Error checking client connection", e);
		}
	}

	public ResponseEntity<byte[]> buildCsvResponse(byte[] csvData, String filename) {
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);
		headers.add(HttpHeaders.CONTENT_TYPE, "text/csv");

		return ResponseEntity.status(HttpStatus.CREATED).headers(headers).body(csvData);
	}
	
	public ResponseEntity<Byte[]> buildCsvResponse(Byte[] csvData, String filename) {
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);
		headers.add(HttpHeaders.CONTENT_TYPE, "text/csv");
		return ResponseEntity.status(HttpStatus.CREATED).headers(headers).body(csvData);
	}

}
