package com.tom.aws.kafka.book;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.tom.aws.kafka.models.Auditable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "book", indexes = {
		@Index(name = "idx_book_title", columnList = "title"),
		@Index(name = "idx_book_isbn", columnList = "isbn"),
		@Index(name = "idx_book_author", columnList = "author"),
		@Index(name = "idx_book_published_date", columnList = "published_date")
})
public class Book extends Auditable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "isbn",
			nullable = false,
			unique = true,
			updatable = false
			)
	private String isbn;
	
	@Column(name = "title",
			length = 200,
			nullable = false,
			unique = false,
			updatable = true
			)
	private String title;
	
	@Column(name = "author",
			nullable = true,
			length = 150,
			unique = false,
			updatable = true
			)
	private String author;
	
	@Column(name = "price", 
			nullable = true,
			unique = false,
			updatable = true
			)
	private BigDecimal price;
	
	@Column(name = "published_date",
			nullable = true,
			unique = false,
			updatable = true
			)
	private LocalDate publishedDate;
	
	@Column(name = "stock_quantity",
			nullable = true,
			unique = false,
			updatable = true
			)
	private Integer stockQuantity;
	
}
