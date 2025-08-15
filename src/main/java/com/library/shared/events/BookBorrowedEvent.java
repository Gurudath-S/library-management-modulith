package com.library.shared.events;

import org.springframework.modulith.events.Externalized;

/**
 * Event published when a book is borrowed
 */
@Externalized("library.book.borrowed::#{#this.transactionId}")
public record BookBorrowedEvent(
    Long transactionId,
    Long bookId,
    Long userId,
    String bookTitle,
    String username,
    long timestamp
) {
    public BookBorrowedEvent(Long transactionId, Long bookId, Long userId, String bookTitle, String username) {
        this(transactionId, bookId, userId, bookTitle, username, System.currentTimeMillis());
    }
}
