CREATE INDEX idx_book_title_fts 
ON book 
USING gin (to_tsvector('english', title));

CREATE INDEX idx_book_author_fts 
ON book 
USING gin (to_tsvector('english', author));
