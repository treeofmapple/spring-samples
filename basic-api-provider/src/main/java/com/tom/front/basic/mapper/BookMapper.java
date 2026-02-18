package com.tom.front.basic.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;

import com.tom.front.basic.dto.BookPageResponse;
import com.tom.front.basic.dto.BookRequest;
import com.tom.front.basic.dto.BookResponse;
import com.tom.front.basic.dto.UpdateRequest;
import com.tom.front.basic.model.Book;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BookMapper {

	// BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);
	
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "isbn", ignore = true)
	Book build(BookRequest request);
	
    @Mapping(source = "createdAt", target = "createdAt") 
    @Mapping(source = "updatedAt", target = "updatedAt") 
	BookResponse toResponse(Book book);
	
	@Mapping(target = "id", ignore = true)
	void updateBookFromRequest(@MappingTarget Book book, UpdateRequest request);
	
	List<BookResponse> toResponseList(List<Book> books);
	
	default BookPageResponse toBookPageResponse(Page<Book> page) {
		if(page == null) {
			return null;
		}
		List<BookResponse> content = toResponseList(page.getContent());
		return new BookPageResponse(content,
				page.getNumber(),
				page.getSize(),
				page.getTotalPages(),
				page.getTotalElements()
			);
	}
	
}
