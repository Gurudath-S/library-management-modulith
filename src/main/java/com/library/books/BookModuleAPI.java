package com.library.books;

import java.util.List;
import java.util.Map;

/**
 * Module API for Book operations
 * This interface defines the public API that other modules can use
 * to interact with the books module
 */
public interface BookModuleAPI {
    
    /**
     * Analytics data structures
     */
    record BookAnalytics(
        long totalBooks,
        long totalCopies,
        long availableCopies,
        long borrowedCopies,
        long outOfStockBooks,
        long lowStockBooks,
        Map<String, Long> booksByCategory,
        List<PopularBook> popularBooks
    ) {}
    
    record PopularBook(
        Long bookId,
        String title,
        String author,
        String category,
        long borrowCount
    ) {}
    
    record BookInfo(
        Long id,
        String isbn,
        String title,
        String author,
        String category,
        int totalCopies,
        int availableCopies,
        boolean isAvailable
    ) {}
    
    /**
     * Get comprehensive book analytics
     */
    BookAnalytics getBookAnalytics();
    
    /**
     * Get book information by ID
     */
    BookInfo getBookInfo(Long bookId);
    
    /**
     * Check if book exists and is available
     */
    boolean isBookAvailable(Long bookId);
    
    /**
     * Borrow a copy of the book (decrements available count)
     */
    void borrowBook(Long bookId);
    
    /**
     * Return a copy of the book (increments available count)
     */
    void returnBook(Long bookId);
    
    /**
     * Get total book count
     */
    long getTotalBooks();
    
    /**
     * Get total copies count
     */
    long getTotalCopies();
    
    /**
     * Get available copies count
     */
    long getAvailableCopies();
    
    /**
     * Get book categories distribution
     */
    Map<String, Long> getBooksByCategory();
    
    /**
     * Get total books count
     */
    long getTotalBooksCount();
    
    /**
     * Get total copies count
     */
    long getTotalCopiesCount();
    
    /**
     * Get available copies count
     */
    long getAvailableCopiesCount();
    
    /**
     * Get out of stock books count
     */
    long getOutOfStockBooksCount();
    
    /**
     * Get low stock books count
     */
    long getLowStockBooksCount(int threshold);
    
    /**
     * Get book count by category
     */
    Map<String, Long> getBookCountByCategory();
    
    /**
     * Get book by ID
     */
    Book getBookById(Long bookId);
    
    /**
     * Get books added after a specific date
     */
    List<Book> getBooksAddedAfter(java.time.LocalDateTime date);
}
