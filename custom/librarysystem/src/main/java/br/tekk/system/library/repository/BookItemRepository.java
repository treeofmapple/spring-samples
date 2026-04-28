package br.tekk.system.library.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.tekk.system.library.model.BookItem;
import br.tekk.system.library.model.Status;

public interface BookItemRepository extends JpaRepository<BookItem, Integer> {

	int countByUserIdAndBookIdAndStatus(UUID userId, Integer bookId, Status status);

}
