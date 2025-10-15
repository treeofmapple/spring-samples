CREATE TABLE book (

    id BIGSERIAL PRIMARY KEY,
    isbn VARCHAR(255) NOT NULL UNIQUE,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    price DECIMAL(19, 2),
    published_date DATE, 
    stock_quantity INTEGER,
    
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    version BIGINT NOT NULL
	
);

-- Comments 

CREATE INDEX IF NOT EXISTS idx_book_title ON book(title);
CREATE INDEX IF NOT EXISTS idx_book_isbn ON book(isbn);
CREATE INDEX IF NOT EXISTS idx_book_author ON book(author);
CREATE INDEX IF NOT EXISTS idx_book_published_date ON book(published_date);

COMMENT ON TABLE book IS 'The book table';

