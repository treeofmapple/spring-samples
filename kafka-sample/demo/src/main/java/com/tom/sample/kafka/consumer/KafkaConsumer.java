package com.tom.sample.kafka.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.tom.sample.kafka.payload.Student;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class KafkaConsumer 
{
	@KafkaListener(topics = "tom", groupId = "masterpiece")
	public void consumeMsg(String msg) 
	{
		log.info(String.format("Consuming the message: %s" , msg));
	}
	
	@KafkaListener(topics = "tom", groupId = "masterpiece")
	public void consumeJsonMsg(Student student) 
	{
		log.info(String.format("Consuming the message: %s", student.toString()));
	}
	
}
