package com.tom.tools.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import com.tom.tools.email.config.EmailSenderService;

import jakarta.mail.MessagingException;

@SpringBootApplication
public class SpringbootEmailSenderApplication {

	@Autowired
	private EmailSenderService service;

	
	@EventListener(ApplicationReadyEvent.class)
	public void triggerMail() throws MessagingException {

		service.sendSimpleEmail("spring.email.to@gmail.com",
				"This is Email Body with Attachment...",
				"This email has attachment");

	}
	
	public static void main(String[] args) {
		SpringApplication.run(SpringbootEmailSenderApplication.class, args);
	}
	
}
