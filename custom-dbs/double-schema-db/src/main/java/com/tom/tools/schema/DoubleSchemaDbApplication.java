package com.tom.tools.schema;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.tom.tools.schema.ds1.BookService;
import com.tom.tools.schema.ds2.AuthorService;

@SpringBootApplication
public class DoubleSchemaDbApplication {

	private final BookService bookService;
	private final AuthorService authorService;

	public DoubleSchemaDbApplication(BookService bookService, AuthorService authorService) {
		this.bookService = bookService;
		this.authorService = authorService;
	}

	@Bean
	ApplicationRunner init() {
		return args -> {
			System.out.println("\n Saving an author (check the MySQL database) ...");
			authorService.persistAuthor();

			System.out.println("\n Saving a book (check the PostgreSQL database) ...");
			bookService.persistBook();
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(DoubleSchemaDbApplication.class, args);
	}

}
