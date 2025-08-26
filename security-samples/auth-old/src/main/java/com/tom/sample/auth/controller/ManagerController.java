package com.tom.sample.auth.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("v1/manager")
@PreAuthorize("hasRole('MANAGER')")
// @Tag(name = "Anonymous") # Swagger
@RequiredArgsConstructor
public class ManagerController {

	// @PreAuthorize("hasAuthority('manager:read')")
	
	// @PreAuthorize("hasAuthority('manager:create')")
	
	// @PreAuthorize("hasAuthority('manager:update')")
	
	// @PreAuthorize("hasAuthority('manager:delete')")
	
}
