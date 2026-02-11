package com.tom.stripe.payment.global.system;

import org.springframework.stereotype.Component;

@Component
public class SystemActionContext {

/*	
	public <T> T runAsSystem(Supplier<T> action) {
		var authorities = List.of(new SimpleGrantedAuthority("ROLE_SYSTEM"));
		var auth = new UsernamePasswordAuthenticationToken("SYSTEM", null, authorities);

		SecurityContextHolder.getContext().setAuthentication(auth);

		try {
			return action.get();
		} finally {
			SecurityContextHolder.clearContext();
		}
	}

*/

}
