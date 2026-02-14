package com.tom.mail.sender.logic.mail;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Collection;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.tom.mail.sender.global.constraints.MailConstraints;
import com.tom.mail.sender.mapper.MailMapper;
import com.tom.mail.sender.model.Mail;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
@RequiredArgsConstructor
public class MailSender {

	private final SimpMessagingTemplate messagingTemplate;
	private final MailMapper mailMapper;
	private final MailBuilder mailBuilder;
	private final MailUtils mailUtils;

	private final Bucket limitBucket = Bucket.builder()
			.addLimit(Bandwidth.builder().capacity(MailConstraints.MAIL_BATCH_SIZE).refillIntervally(
					MailConstraints.MAIL_BATCH_SIZE, Duration.ofSeconds(MailConstraints.MAIL_WAIT_TIME)).build())
			.build();

	@Async("mailTaskExecutor")
	public void sendEmailWithAttachment(Mail mail) {
		mailUtils.testConnection();
		Collection<String> recipients = mail.getUsers();
		int total = mail.getUsers().size();
		int current = 0;

		String rawTemplate = new String(mail.getContent(), StandardCharsets.UTF_8);

		for (String recipient : recipients) {
			try {
				if (current % MailConstraints.MAIL_BATCH_SIZE == 0) {
					int remaining = total - current;
					int currentBatchSize = Math.min(MailConstraints.MAIL_BATCH_SIZE, remaining);

					if (currentBatchSize > 0) {
						limitBucket.asBlocking().consume(currentBatchSize);
						log.info("Throttling: Consuming tokens for next batch of {}", currentBatchSize);
					}
				}

				mailBuilder.buildMail(mail, rawTemplate, recipient);
				current++;

				double progress = (double) current / total * 100;
				messagingTemplate.convertAndSend("/topic/email-progress",
						mailMapper.toResponse(current, total, progress));
			} catch (InterruptedException e) {
				log.error("Email task interrupted", e);
				Thread.currentThread().interrupt();
				break;
			}
		}
		log.info("Email campaign finished. Total sent: {}/{}", current, total);
	}

}
