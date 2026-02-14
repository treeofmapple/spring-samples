package com.tom.mail.sender.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tom.mail.sender.global.system.ConfigInfo;
import com.tom.mail.sender.global.system.SystemInfo;

import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequestMapping("/config")
@RequiredArgsConstructor
public class ConfigController {

	private final ConfigInfo configInfo;

	@GetMapping
	public ResponseEntity<SystemInfo> systemConfigInfo() {
		var response = configInfo.fetchSystemInformations();
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
}