package br.tekk.system.library.request.dto;

import java.util.List;
import java.util.stream.Collectors;

import br.tekk.system.library.model.BookItem;

public record UserDTOBookItems(List<BookItemSummary> items) {
    public static UserDTOBookItems fromBookItems(List<BookItem> bookItems) {
        if (bookItems == null) {
            return new UserDTOBookItems(List.of());
        }

        List<BookItemSummary> summaries = bookItems.stream()
                .map(item -> new BookItemSummary(item.getId(), item.getBook().getTitle(), item.getRentStart(), item.getRentEnd()))
                .collect(Collectors.toList());

        return new UserDTOBookItems(summaries);
    }
}