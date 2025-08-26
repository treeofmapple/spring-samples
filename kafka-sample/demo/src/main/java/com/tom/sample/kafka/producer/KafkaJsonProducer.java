package com.tom.sample.kafka.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import com.tom.sample.kafka.payload.Student;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaJsonProducer 
{

	private final KafkaTemplate<String, String> kafkaTemplate;
	
	public void sendMessage(Student student) 
	{
		Message<Student> message = MessageBuilder
				.withPayload(student)
				.setHeader(KafkaHeaders.TOPIC, "Tom")
				.build();
		kafkaTemplate.send(message);
	}
	
}
