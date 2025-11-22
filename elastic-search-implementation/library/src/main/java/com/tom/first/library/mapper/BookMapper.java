package com.tom.first.library.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.tom.first.library.dto.BookItemResponse;
import com.tom.first.library.dto.BookOutboxBuilder;
import com.tom.first.library.dto.BookRequest;
import com.tom.first.library.dto.BookResponse;
import com.tom.first.library.model.Book;
import com.tom.first.library.model.BookDocument;
import com.tom.first.library.model.BookOutbox;
import com.tom.first.library.model.RentHistory;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BookMapper {

	@Mapping(target = "id", ignore = true)
	Book build(BookRequest request);
	
	@Mapping(target = "id", ignore = true)
	Book build(BookDocument doc);
	
	@Mapping(target = "id", ignore = true)
	BookDocument build(Book book);
	
	List<BookDocument> build(List<Book> books);
	
	BookOutbox build(BookOutboxBuilder outboxBuild);
	
	BookResponse toResponse(Book Book);
	
	@Mapping(target = "username", ignore = true)
    @Mapping(target = "bookTitle", source = "bookItem.book.title")
    @Mapping(target = "price", source = "bookItem.book.price")
	BookItemResponse toResponse(RentHistory rentHistory);
}
