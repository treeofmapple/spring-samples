CREATE TABLE auditable (
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    version BIGINT NOT NULL
);

CREATE TABLE product (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    quantity INTEGER,
    price DECIMAL(19, 2),
    manufacturer VARCHAR(255),
    active BOOLEAN NOT NULL DEFAULT TRUE
    
) INHERITS (auditable);

CREATE TABLE image (
    id BIGSERIAL PRIMARY KEY,
    image_name VARCHAR(255) NOT NULL,
    object_key VARCHAR(255) NOT NULL UNIQUE,
    object_url VARCHAR(255) NOT NULL UNIQUE,
    content_type VARCHAR(255) NOT NULL,
    size BIGINT NOT NULL

) INHERITS (auditable);

CREATE TABLE book (

    id BIGSERIAL PRIMARY KEY,
    isbn VARCHAR(255) NOT NULL UNIQUE,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    price DECIMAL(19, 2),
    published_date DATE, 
    stock_quantity INTEGER
    
) INHERITS (auditable);

-- INDEXES
CREATE INDEX IF NOT EXISTS idx_product_name ON product(name);
CREATE INDEX IF NOT EXISTS idx_image_name ON image(image_name);
CREATE INDEX IF NOT EXISTS idx_book_title ON book(title);
CREATE INDEX IF NOT EXISTS idx_book_isbn ON book(isbn);
CREATE INDEX IF NOT EXISTS idx_book_author ON book(author);
CREATE INDEX IF NOT EXISTS idx_book_published_date ON book(published_date);

-- COMMENTS 

COMMENT ON TABLE book IS 'The book table';
COMMENT ON TABLE product IS 'Stores general product information.';
COMMENT ON TABLE image IS 'Stores metadata for uploaded images, like S3 object keys and URLs.';

