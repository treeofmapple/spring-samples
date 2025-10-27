package com.tom.tools.doubledb;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.tom.tools.doubledb.ds1.AuthorService;
import com.tom.tools.doubledb.ds2.BookService;

@SpringBootApplication
public class DoubleDatabasesApplication {

	private final AuthorService authorService;
	private final BookService bookService;

	public DoubleDatabasesApplication(AuthorService authorService, BookService bookService) {
		this.authorService = authorService;
		this.bookService = bookService;
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
		SpringApplication.run(DoubleDatabasesApplication.class, args);
	}

}
