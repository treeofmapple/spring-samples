package com.tom.tools.rabbitmq.producer.producing;

import java.util.Date;
import java.util.UUID;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tom.tools.rabbitmq.producer.config.RabbitConfig;

@RestController
@RequestMapping("/publish")
public class MessagePublisher {

	@Autowired
	private RabbitTemplate template;

	@PostMapping
	public String publishMessage(@RequestBody MessageModel message) throws AmqpException {
		message.setMessageId(UUID.randomUUID().toString());
		message.setMessageDate(new Date());
		template.convertAndSend(RabbitConfig.EXCHANGE, RabbitConfig.ROUTING_KEY, message);

		return "Message Published";
	}

}
