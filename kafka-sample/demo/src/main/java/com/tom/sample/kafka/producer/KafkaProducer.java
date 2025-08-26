package com.tom.sample.kafka.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducer 
{

	private final KafkaTemplate<String, String> kafkaTemplate;
	
	public void sendMessage(String msg) 
	{
		log.info(String.format("Sending message: %s", msg));
		kafkaTemplate.send("Tom:", msg);
	}
	
}
