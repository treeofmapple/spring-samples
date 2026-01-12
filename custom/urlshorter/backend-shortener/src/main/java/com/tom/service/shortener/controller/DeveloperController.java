package com.tom.service.shortener.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tom.service.shortener.dto.URLComplete;
import com.tom.service.shortener.service.URLDeveloper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class DeveloperController {

	private final URLDeveloper service;

	@GetMapping("dev/full")
	public ResponseEntity<URLComplete> findFullURL(@RequestParam("url") String request) {
		var urls = service.findFullURL(request);
		return ResponseEntity.status(HttpStatus.OK).body(urls);
	}

	@DeleteMapping("dev/remove/{delete}")
	public ResponseEntity<Void> deleteURL(@PathVariable("delete") String request) {
		service.deleteURL(request);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

}
