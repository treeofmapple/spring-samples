package com.tom.sample.producers.stream;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.tom.sample.producers.producer.WikimediaProducer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class WikimediaStreamConsumer 
{

	private final WikimediaProducer producer;
	private final WebClient webClient;
	
	public WikimediaStreamConsumer(
			WikimediaProducer producer,
			WebClient.Builder webClient) 
	{
		this.webClient = webClient
				.baseUrl("https://stream.wikimedia.org/v2")
				.build();
		this.producer = producer;
	}
	
	public void consumeStreamAndPublish() 
	{
		webClient.get()
		.uri("/stream/recentchange")
		.retrieve()
		.bodyToFlux(String.class)
		.subscribe(producer::sendMessage);
	}
	
	
}
