package com.tom.sample.producers.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class WikimediaProducer 
{
	private final KafkaTemplate<String, String> kafkaTemplate;
	
	public void sendMessage(String msg)
	{
		log.info("Sending message to Topic: %s", msg);
		kafkaTemplate.send("wikimedia-stream", msg);
	}
	
}
