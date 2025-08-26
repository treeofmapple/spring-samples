package com.tom.sample.producers.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tom.sample.producers.stream.WikimediaStreamConsumer;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/wikimedia")
@RequiredArgsConstructor
public class WikimediaController 
{

	private final WikimediaStreamConsumer consumer;
	
	@GetMapping
	public void startPublishing() 
	{
		consumer.consumeStreamAndPublish();
	}
	
}
