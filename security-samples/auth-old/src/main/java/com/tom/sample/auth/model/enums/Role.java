package com.tom.sample.auth.model.enums;

import static com.tom.sample.auth.model.enums.Permission.ADMIN_CREATE;
import static com.tom.sample.auth.model.enums.Permission.ADMIN_DELETE;
import static com.tom.sample.auth.model.enums.Permission.ADMIN_READ;
import static com.tom.sample.auth.model.enums.Permission.ADMIN_UPDATE;
import static com.tom.sample.auth.model.enums.Permission.MANAGER_CREATE;
import static com.tom.sample.auth.model.enums.Permission.MANAGER_DELETE;
import static com.tom.sample.auth.model.enums.Permission.MANAGER_READ;
import static com.tom.sample.auth.model.enums.Permission.MANAGER_UPDATE;
import static com.tom.sample.auth.model.enums.Permission.USER_CREATE;
import static com.tom.sample.auth.model.enums.Permission.USER_DELETE;
import static com.tom.sample.auth.model.enums.Permission.USER_READ;
import static com.tom.sample.auth.model.enums.Permission.USER_UPDATE;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Role {

	ANONYMOUS(Collections.emptySet()),
	USER(Set.of(
			USER_READ,
			USER_UPDATE,
			USER_DELETE,
			USER_CREATE
		)
	),	
	ADMIN(Set.of(
			ADMIN_READ,
			ADMIN_UPDATE,
			ADMIN_DELETE,
			ADMIN_CREATE,
			MANAGER_READ,
			MANAGER_UPDATE,
			MANAGER_CREATE,
			MANAGER_DELETE,
			USER_READ,
			USER_UPDATE,
			USER_DELETE,
			USER_CREATE
		)
	),
	MANAGER(Set.of(
			MANAGER_READ,
			MANAGER_UPDATE,
			MANAGER_CREATE,
			MANAGER_DELETE,
			USER_READ,
			USER_UPDATE,
			USER_DELETE,
			USER_CREATE
			
		)
	)
	;

	@Getter
	private final Set<Permission> permissions;

	public List<SimpleGrantedAuthority> getAuthorities() {
		var authorities = getPermissions()
				.stream()
				.map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
				.collect(Collectors.toList());
		authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
		return authorities;
	}
	
}
