package com.tom.first.datajpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tom.first.datajpa.model.Author;

import jakarta.transaction.Transactional;

public interface AuthorRepository extends JpaRepository<Author, Integer>, JpaSpecificationExecutor<Author> {
	
	@Transactional
	List <Author> findByNamedQuery(@Param("age") int age);
	
	@Modifying
	@Transactional
	void updateByNamedQuery(@Param("age") int age);
	
    // UPDATE Author a SET a.age = 22 WHERE a.id = 1
	@Modifying
	@Transactional
	@Query("UPDATE Author a SET a.age = :age WHERE a.id = :id")
	int updateAuthor(int age, int id);
	
	@Modifying
	@Transactional
	@Query("UPDATE Author a SET a.age = :age")
	void updateAllAuthorsAges(int age); 
	
	// SELECT * FROM Author WHERE first_name = 'ali';
	List<Author> findAllByFirstName(String fn);

	// SELECT * FROM Author WHERE first_name = 'al';
	List<Author> findAllByFirstNameIgnoreCase(String fn);
	
	// SELECT * FROM Author WHERE first_name LIKE '%al%'
	List<Author> findAllByFirstNameContainingIgnoreCase(String fn);
	
	// SELECT * FROM Author WHERE first_name LIKE 'al%'
	List<Author> findAllByFirstNameStartsWithIgnoreCase(String fn);

	// SELECT * FROM Author WHERE first_name LIKE '%al'
	List<Author> findAllByFirstNameEndsWithIgnoreCase(String fn);

	// SELECT * FROM Author WHERE first_name IN ('ali', 'bou', 'coding')
	List<Author> findAllByFirstNameInIgnoreCase(List<String> firstNames);
	
}
