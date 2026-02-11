package com.tom.mail.sender.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tom.mail.sender.dto.MailRequest;
import com.tom.mail.sender.dto.MailResponse;
import com.tom.mail.sender.dto.MailUpdateRequest;
import com.tom.mail.sender.dto.MasterMailRequest;
import com.tom.mail.sender.dto.PageMailResponse;
import com.tom.mail.sender.service.MailService;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequestMapping("/v1/mail")
@RequiredArgsConstructor
public class MailController {

	private final MailService mailService;

	@GetMapping(value = "/search", 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PageMailResponse> searchMailByParams(
			@RequestParam(defaultValue = "0", required = false) @Min(0) int page,
			@RequestParam(required = false) String title
			/* , @RequestParam(required = false) // user will be a list can be one or multiple */) {
		var response = mailService.searchMailByParams(page, title); 
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<MailResponse> searchMailById(@PathVariable(value = "id") UUID userId) {
		var response = mailService.searchMailById(userId);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@PostMapping
	public ResponseEntity<MailResponse> createMail(@RequestBody MailRequest request) {
		var response = mailService.createMail(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PostMapping(value = "/send/{title}")
	public ResponseEntity<MailResponse> sendMail(@PathVariable(value = "title") String titleMail) {
		var response = mailService.sendMail(titleMail);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@PostMapping(value = "/send/only")
	public ResponseEntity<MailResponse> sendMailToOnlyUsers() {
		var response = mailService.sendMailToOnlyUsers();
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@PutMapping
	public ResponseEntity<MailResponse> defineMasterEmail(@RequestBody MasterMailRequest request) {
		var response = mailService.defineMasterMail(request);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<MailResponse> editMailInfo(@RequestBody MailUpdateRequest request) {
		var response = mailService.editMailInfo(request);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> deleteMail(@PathVariable(value = "id") UUID mailId) {
		mailService.deleteMail(mailId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

}
