package com.tom.kafka.producer.book;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BookMapper {

	BookResponse toResponse(Book book);
	
	@Mapping(target = "id", ignore = true)
	Book updateBookFromData(@MappingTarget Book book, String isbn, String title, String author, BigDecimal price,
			LocalDate publishedDate, Integer stockQuantity);

	BookDTO toKafka(Book book);
	
}
