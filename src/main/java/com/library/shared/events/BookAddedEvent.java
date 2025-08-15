package com.library.shared.events;

import org.springframework.modulith.events.Externalized;

/**
 * Event published when a book is added to the library
 */
@Externalized("library.book.added::#{#this.bookId}")
public record BookAddedEvent(
    Long bookId,
    String title,
    String author,
    String isbn,
    String category,
    int totalCopies,
    long timestamp
) {
    public BookAddedEvent(Long bookId, String title, String author, String isbn, String category, int totalCopies) {
        this(bookId, title, author, isbn, category, totalCopies, System.currentTimeMillis());
    }
}
