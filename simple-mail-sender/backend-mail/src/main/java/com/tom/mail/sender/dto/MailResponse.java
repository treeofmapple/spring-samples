package com.tom.mail.sender.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record MailResponse(
		
		UUID id,
		String title,
		String content,
		List<String> users,
		List<LocalDateTime> sentOnTime
		
) {

}
