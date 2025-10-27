package com.tom.tools.rabbitmq.consumer.consuming;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.tom.tools.rabbitmq.consumer.config.RabbitConfig;

@Component
public class MessageListener {

	@RabbitListener(queues = RabbitConfig.QUEUE)
	public void listener(MessageModel message) {
		System.out.println(message);
	}

	
}
