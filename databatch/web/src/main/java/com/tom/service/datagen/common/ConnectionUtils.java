package com.tom.service.datagen.common;

import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class ConnectionUtils {

	public ResponseEntity<byte[]> buildCsvResponse(byte[] csvData, String filename) {
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);
		headers.add(HttpHeaders.CONTENT_TYPE, "text/csv");

		return ResponseEntity.status(HttpStatus.CREATED).headers(headers).body(csvData);
	}

	public ResponseEntity<byte[]> buildZipResponse(byte[] zipData, String filename) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headers.setContentDisposition(ContentDisposition.attachment().filename(filename).build());
		return new ResponseEntity<>(zipData, headers, HttpStatus.OK);
	}

}
