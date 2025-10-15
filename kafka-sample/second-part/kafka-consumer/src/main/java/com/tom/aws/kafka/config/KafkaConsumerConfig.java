package com.tom.aws.kafka.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tom.aws.kafka.book.BookDTO;

@Configuration
public class KafkaConsumerConfig {

	@Value("${spring.kafka.consumer.bootstrap-servers}")
	private String bootstrapServers;

	@Value("${spring.kafka.consumer.group-id}")
	private String groupId;

	@Bean
	Map<String, Object> consumerConfigs() {
		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
		return props;
	}

	@Bean
	ConsumerFactory<String, BookDTO> consumerFactory() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
		
		JsonDeserializer<BookDTO> jsonDeserializer = new JsonDeserializer<>(BookDTO.class, objectMapper);
        jsonDeserializer.setUseTypeHeaders(false);
        jsonDeserializer.addTrustedPackages("*");

        ErrorHandlingDeserializer<BookDTO> errorHandlingDeserializer = new ErrorHandlingDeserializer<>(jsonDeserializer);

		return new DefaultKafkaConsumerFactory<>(
				consumerConfigs(), 
				new StringDeserializer(),
				errorHandlingDeserializer); 
		
	}

	@Bean
	ConcurrentKafkaListenerContainerFactory<String, BookDTO> kafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, BookDTO> factory = 
				new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory());
		return factory;
	}

}
