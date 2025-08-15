package com.library.shared.events;

import org.springframework.modulith.events.Externalized;

/**
 * Event published when a book is returned
 */
@Externalized("library.book.returned::#{#this.transactionId}")
public record BookReturnedEvent(
    Long transactionId,
    Long bookId,
    Long userId,
    String bookTitle,
    String username,
    boolean wasOverdue,
    long timestamp
) {
    public BookReturnedEvent(Long transactionId, Long bookId, Long userId, String bookTitle, String username, boolean wasOverdue) {
        this(transactionId, bookId, userId, bookTitle, username, wasOverdue, System.currentTimeMillis());
    }
}
