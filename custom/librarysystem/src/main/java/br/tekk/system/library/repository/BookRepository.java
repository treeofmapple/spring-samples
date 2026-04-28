package br.tekk.system.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.tekk.system.library.model.Book;

public interface BookRepository extends JpaRepository<Book, Integer> {

	boolean existsByTitle(String title);

}
