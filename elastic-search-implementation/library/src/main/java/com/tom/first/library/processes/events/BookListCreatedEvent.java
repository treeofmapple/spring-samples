package com.tom.first.library.processes.events;

import java.util.List;

import com.tom.first.library.model.Book;

public record BookListCreatedEvent(List<Book> books) {

}
