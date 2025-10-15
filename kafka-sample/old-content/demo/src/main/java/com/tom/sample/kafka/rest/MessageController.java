package com.tom.sample.kafka.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tom.sample.kafka.payload.Student;
import com.tom.sample.kafka.producer.KafkaJsonProducer;
import com.tom.sample.kafka.producer.KafkaProducer;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/v1/messages")
@RequiredArgsConstructor
public class MessageController 
{
	
	private final KafkaProducer producer;
	private final KafkaJsonProducer jsonProducer;
	
	@PostMapping
	public ResponseEntity<String> sendMessage(
			@RequestBody String message
			)
	{
		producer.sendMessage(message);
		return ResponseEntity.status(HttpStatus.OK).body("Message queued");
	}

	@PostMapping(value = "/json")
	public ResponseEntity<String> jsonMessage(
			@RequestBody Student message
			)
	{
		jsonProducer.sendMessage(message);
		return ResponseEntity.status(HttpStatus.OK).body("Message queued as JSON");
	}
}
