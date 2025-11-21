package com.tom.first.library.processes.events;

import com.tom.first.library.model.Book;

public record BookCreatedEvent(Book book) {

}
