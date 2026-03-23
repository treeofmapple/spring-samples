package com.tom.stripe.payment.user.dto;

import java.util.UUID;

public record SimpleUserResponse(
		
		UUID id,
		String nickname,
		String email
		
) {

}
