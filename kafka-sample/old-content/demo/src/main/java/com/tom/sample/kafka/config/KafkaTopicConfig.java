package com.tom.sample.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig 
{

	@Bean
	NewTopic tomTopic() 
	{
		return TopicBuilder.name("tom").build();
	}

}
