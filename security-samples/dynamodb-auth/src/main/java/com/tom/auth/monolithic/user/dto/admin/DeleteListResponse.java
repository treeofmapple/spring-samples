package com.tom.auth.monolithic.user.dto.admin;

import java.util.List;
import java.util.UUID;

public record DeleteListResponse(
		
	    List<UUID> deletedUserIds
	    
		) {

}
