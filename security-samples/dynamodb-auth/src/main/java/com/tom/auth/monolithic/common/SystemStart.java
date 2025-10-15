package com.tom.auth.monolithic.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.tom.auth.monolithic.user.model.User;
import com.tom.auth.monolithic.user.model.enums.Role;
import com.tom.auth.monolithic.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
@RequiredArgsConstructor
public class SystemStart implements CommandLineRunner {

	@Value("${settings.security.generated.user}")
	private String username;
	
	@Value("${settings.security.generated.email}")
	private String email;
	
	@Value("${settings.security.generated.password}")
	private String password;
	
	private final PasswordEncoder passwordEncoder;
	private final UserRepository repository;
	
	@Override
	public void run(String... args) throws Exception {
		generateAdminUser();
	}
	
	public void generateAdminUser() {
		if(!repository.existsByUsername(username)) {
			User user = new User();
			
			user.setUsername(username);
			user.setEmail(email);
			user.setPassword(passwordEncoder.encode(password));
			user.setRole(Role.ADMIN);
            user.setVersion(0L);
			
			repository.save(user);
			log.info(">>>> Default ADMIN user created successfully: {}", user);
        } else {
            log.info(">>>> Default ADMIN user '{}' already exists. Skipping creation.", username);
        }
	}
	
	
}
