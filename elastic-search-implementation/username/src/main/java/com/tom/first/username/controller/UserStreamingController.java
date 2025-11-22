package com.tom.first.username.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tom.first.username.service.UserDataStreaming;

import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequestMapping("/v1/username/streaming")
@RequiredArgsConstructor
public class UserStreamingController {

	private final UserDataStreaming dataStreaming;

	@PostMapping(value = "/start")
	public ResponseEntity<Void> startDataStreaming(@RequestParam(required = false, defaultValue = "200") int speed) {
		dataStreaming.startCreatingUsers(speed);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@PostMapping(value = "/stop")
	public ResponseEntity<Void> stopDataStreaming() {
		dataStreaming.stopCreatingUsers();
		return ResponseEntity.status(HttpStatus.OK).build();
	}

}
