package com.tom.mail.sender.dto;

public record MailProgressResponse(
		
		Integer current,
		Integer total,
		Double percent
		
) {

}
