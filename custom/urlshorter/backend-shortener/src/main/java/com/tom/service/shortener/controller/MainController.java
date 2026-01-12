package com.tom.service.shortener.controller;


import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tom.service.shortener.dto.URLRequest;
import com.tom.service.shortener.dto.URLResponse;
import com.tom.service.shortener.service.URLService;

import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class MainController {

	private final URLService service;
	
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<URLResponse> shortenURL(@RequestBody URLRequest request) {
		var redirectUrl = service.shortenURL(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(redirectUrl);
	}
    
	@GetMapping(value = "/{request}")
	public ResponseEntity<Void> redirectURL(@PathVariable @NotBlank String request) {
		var originalURL = service.redirectURL(request);
		return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(originalURL)).build();
	}
    
	@PatchMapping(value = "/extend", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<URLResponse> extendUrlExpiration(@RequestParam("url") @NotBlank String request) {
		var updatedUrl = service.extendUrlExpiration(request);
		return ResponseEntity.status(HttpStatus.OK).body(updatedUrl);
	}
	
	@GetMapping(value = "/info", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<URLResponse> findBasicURL(@RequestParam(value = "url") String request) {
		var urls = service.findBasicURL(request);
		return ResponseEntity.status(HttpStatus.OK).body(urls);
	}

}
