package com.tom.sample.consumers.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class WikimediaConsumer {

	@KafkaListener(topics = "wikimedia-stream", groupId = "masterpiece")
	public void consumeMsg(String msg) 
	{
		log.info(String.format("Consuming the message from wikimedia-stream: %s", msg));
	}
	
}
