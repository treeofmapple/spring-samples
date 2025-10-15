package com.tom.auth.monolithic.user.model.enums;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Role {

	ANONYMOUS(Collections.emptySet()),
	
	USER(Permission.USER_PERMISSIONS),
	
	ADMIN(Stream.concat(Permission.USER_PERMISSIONS.stream(),
			 Permission.ADMIN_PERMISSIONS.stream()).collect(Collectors.toSet()))
	;
	
	@Getter
	private final Set<Permission> permissions;
	
	private List<SimpleGrantedAuthority> authorities;
	
	public List<SimpleGrantedAuthority> getAuthorities() {
		if(authorities == null) {
            List<SimpleGrantedAuthority> grantedAuthorities = this.permissions.stream()
                    .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                    .collect(Collectors.toList());
            
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
            
            this.authorities = Collections.unmodifiableList(grantedAuthorities);
		}
        return this.authorities;
	}
	
}
