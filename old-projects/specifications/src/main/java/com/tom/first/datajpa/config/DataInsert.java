package com.tom.first.datajpa.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.tom.first.datajpa.model.Author;
import com.tom.first.datajpa.repository.AuthorRepository;
import com.tom.first.datajpa.service.specification.AuthorSpecification;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;

@Component
@RequiredArgsConstructor
public class DataInsert {

	private final Faker faker = new Faker();
	private Map<Author, Integer> hashMap = new HashMap<>();
	private static int QUANTITY = 30;
	private final AuthorRepository authorRepository;
	// private final VideoRepository videoRepository; unsued
	
    @PostConstruct
    public void init() {
        generateAuthors();
        populateTables();
    }
	
	public void generateAuthors() {
		
		for(int i = 0; i < QUANTITY; i++) {
			var author = Author.builder()
					.firstName(faker.name().firstName())
					.lastName(faker.name().lastName())
					.age(faker.number().numberBetween(19, 59))
					.email(faker.internet().emailAddress())
					.build();

			hashMap.put(author, i);
		}
		
		var base = Author.builder()
				.id(1)
				.firstName("Vander")
				.lastName("Ether")
				.age(32)
				.email("Contra@hotmail.com")
				.build();
		hashMap.put(base, (QUANTITY + 1));
	}
	
	public void populateTables() {

		hashMap.keySet().forEach(authorRepository::save);

		Specification<Author> spec = Specification.where(AuthorSpecification.hasAge(32));
		Specification<Author> nameLike = spec.and(AuthorSpecification.firstNameLike("Mi"));

		authorRepository.findAll(spec).forEach(System.out::println);
		authorRepository.findByNamedQuery(40).forEach(System.out::println);
		authorRepository.findAll(nameLike).forEach(System.out::println);
	}
	
	
}
