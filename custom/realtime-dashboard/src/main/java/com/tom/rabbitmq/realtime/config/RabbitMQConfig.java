package com.tom.rabbitmq.realtime.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Configuration
public class RabbitMQConfig {

	@Bean
	Jackson2JsonMessageConverter jsonConverter() {
		return new Jackson2JsonMessageConverter();
	}

	@Bean
	TopicExchange vehicleTelemetryExchange() {
		return new TopicExchange(RabbitMQTopics.VEHICLE_TELEMETRY_EXCHANGE);
	}

	@Bean
	Queue vehicleTelemetryQueue() {
		return QueueBuilder.durable(RabbitMQTopics.VEHICLE_TELEMETRY_QUEUE)
				.withArgument("x-queue-mode", "default")
				.withArgument("x-max-priority", 10)
				.build();
	}

	@Bean
	Binding telemetryBinding() {
		return BindingBuilder.bind(vehicleTelemetryQueue())
				.to(vehicleTelemetryExchange())
				.with(RabbitMQTopics.VEHICLE_TELEMETRY_DEFAULT_KEY);
	}

	@Bean
	RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
			Jackson2JsonMessageConverter messageConverter, RetryTemplate retry) {
		RabbitTemplate template = new RabbitTemplate(connectionFactory);
		template.setMessageConverter(messageConverter);
		template.setRetryTemplate(retry);
		template.setMandatory(true);
		template.setConfirmCallback((correlationData, ack, reason) -> {
			if (ack) {
				log.debug("Message sent successfully!");
			} else {
				log.error("Message failed: " + reason);
			}
		});

		template.setReturnsCallback(returned -> {
			log.error("Message was not routed to any queue: {}", returned.getMessage());
		});

		return template;
	}

	@Bean
	RetryTemplate retryTemplate() {
		RetryTemplate retry = new RetryTemplate();
		retry.setRetryPolicy(new SimpleRetryPolicy(5));
		ExponentialBackOffPolicy back = new ExponentialBackOffPolicy();
		back.setInitialInterval(500);
		back.setMultiplier(2);
		retry.setBackOffPolicy(back);
		return retry;
	}

}
