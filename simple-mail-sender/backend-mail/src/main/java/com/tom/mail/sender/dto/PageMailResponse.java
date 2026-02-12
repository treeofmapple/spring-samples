package com.tom.mail.sender.dto;

import java.util.List;

public record PageMailResponse(
		
		List<MailResponse> content,
		Integer page,
		Integer size,
		Integer totalPages,
		Long totalElements
		
) {

}
