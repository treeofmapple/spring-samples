package br.tekk.system.library;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class LibrarySystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(LibrarySystemApplication.class, args);
	}

	// DESIGN: make the system design and develop some new features
	
	// DOCUMENT: Finish the system documentation

	// DOCUMENT: Implement Swagger config on controllers and on the exceptions
	
}


