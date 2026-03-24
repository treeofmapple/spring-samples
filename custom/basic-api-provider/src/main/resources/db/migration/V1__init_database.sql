CREATE TABLE book (
	
	id BIGSERIAL PRIMARY KEY,
	
	isbn VARCHAR(255) NOT NULL UNIQUE,
	title VARCHAR(200) NOT NULL,
	author VARCHAR(150) NOT NULL,
	price NUMERIC(19,2),
	published_date DATE,
	stock_quantity INTEGER,
	
	created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
	updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL	
	
);

CREATE INDEX idx_book_title ON book (title);
CREATE INDEX idx_book_isbn ON book (isbn);
CREATE INDEX idx_book_author ON book (author);
CREATE INDEX idx_book_published_date ON book (published_date);

COMMENT ON TABLE book IS 'Stores book information, which is a type of product.';