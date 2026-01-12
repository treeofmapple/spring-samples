package com.tom.aws.awstest.controller;

import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tom.aws.awstest.image.ImageService;
import com.tom.aws.awstest.image.dto.ImageResponse;
import com.tom.aws.awstest.image.dto.PageImageResponse;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/image")
@RequiredArgsConstructor
public class ImageController {

	private final ImageService service;

	@GetMapping(value = "/search", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PageImageResponse> findByObjectParams(@RequestParam(defaultValue = "0") @Min(0) int page,
			@RequestParam(required = false) String query) {
		var response = service.searchObjectByParams(page, query);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@GetMapping(value = "/search/id", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ImageResponse> findImageById(@RequestParam long query) {
		var response = service.searchObjectById(query);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ImageResponse> uploadImageToNote(@RequestParam MultipartFile file) {
		var response = service.uploadObject(file);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping(value = "/download/{name}", produces = "application/zip")
	public ResponseEntity<Resource> downloadObject(@PathVariable("name") String objectName) {
		var response = service.downloadObject(objectName);

		ContentDisposition contentDisposition = ContentDisposition.builder("attachment").filename(response.fileName())
				.build();

		return ResponseEntity.status(HttpStatus.OK)
				.header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString())
				.contentType(MediaType.APPLICATION_OCTET_STREAM).contentLength(response.fileSize())
				.body(response.resource());
	}

	@PutMapping(value = "/edit/{name}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ImageResponse> renameObject(@PathVariable("name") String objectName,
			@RequestBody(required = true) @NotBlank(message = "Object new name cannot be blank") String newName) {
		var response = service.renameObject(objectName, newName);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@DeleteMapping(value = "/remove")
	public ResponseEntity<Void> removeObject(@PathVariable("name") String objectName) {
		service.removeObject(objectName);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@PostMapping(value = "/data/generate", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PageImageResponse> generateImages(@RequestParam @Min(1) int quantity) {
		var response = service.generateImages(quantity);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

}
