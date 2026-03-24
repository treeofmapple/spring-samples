package com.tom.aws.awstest.product.dto;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public record ProductResponse(
        
        long id,
        String name,
        int quantity,
        BigDecimal price,
        String manufacturer,
        boolean active,
	    ZonedDateTime createdAt,
	    ZonedDateTime updatedAt
	    
		) {
}
