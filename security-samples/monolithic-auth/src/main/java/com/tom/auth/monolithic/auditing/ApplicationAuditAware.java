package com.tom.auth.monolithic.auditing;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.tom.auth.monolithic.user.model.User;

public class ApplicationAuditAware implements AuditorAware<UUID> {

	@Override
	public Optional<UUID> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()
				|| authentication instanceof AnonymousAuthenticationToken) {
			return Optional.empty();
        }
        
        Object principal = authentication.getPrincipal();
        if (principal instanceof User) {
            User userPrincipal = (User) principal;
            return Optional.ofNullable(userPrincipal.getId());
        }
        
		return Optional.empty();
	}

}
