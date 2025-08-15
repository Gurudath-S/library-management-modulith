package com.library.transactions;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Module API for Transaction operations
 * This interface defines the public API that other modules can use
 * to interact with the transactions module
 */
public interface TransactionModuleAPI {
    
    /**
     * Analytics data structures
     */
    record TransactionAnalytics(
        long totalTransactions,
        long activeTransactions,
        long completedTransactions,
        long overdueTransactions,
        long transactionsThisMonth,
        double averageBorrowingDuration
    ) {}
    
    record TransactionInfo(
        Long id,
        Long userId,
        Long bookId,
        String type,
        String status,
        LocalDateTime borrowedAt,
        LocalDateTime dueDate,
        LocalDateTime returnedAt,
        boolean isOverdue
    ) {}
    
    record BorrowingPattern(
        String period,
        long borrowCount,
        long returnCount
    ) {}
    
    /**
     * Get comprehensive transaction analytics
     */
    TransactionAnalytics getTransactionAnalytics();
    
    /**
     * Get transaction information by ID
     */
    TransactionInfo getTransactionInfo(Long transactionId);
    
    /**
     * Get user's transaction history
     */
    List<TransactionInfo> getUserTransactions(Long userId);
    
    /**
     * Get book's transaction history
     */
    List<TransactionInfo> getBookTransactions(Long bookId);
    
    /**
     * Get overdue transactions
     */
    List<TransactionInfo> getOverdueTransactions();
    
    /**
     * Borrow a book
     */
    TransactionInfo borrowBook(Long userId, Long bookId);
    
    /**
     * Return a book
     */
    TransactionInfo returnBook(Long userId, Long bookId);
    
    /**
     * Get total transaction count
     */
    long getTotalTransactions();
    
    /**
     * Get active transaction count
     */
    long getActiveTransactions();
    
    /**
     * Get overdue transaction count
     */
    long getOverdueTransactionsCount();
    
    /**
     * Get transactions this month
     */
    long getTransactionsThisMonth();
    
    /**
     * Get total transactions count
     */
    long getTotalTransactionsCount();
    
    /**
     * Get active transactions count
     */
    long getActiveTransactionsCount();
    
    /**
     * Get completed transactions count
     */
    long getCompletedTransactionsCount();
    
    /**
     * Get transactions in a specific month
     */
    long getTransactionsInMonth(int year, int month);
    
    /**
     * Get average borrowing duration in days
     */
    double getAverageBorrowingDuration();
    
    /**
     * Get book borrow counts (book ID -> borrow count)
     */
    java.util.Map<Long, Long> getBookBorrowCounts();
    
    /**
     * Get borrows in a specific month
     */
    long getBorrowsInMonth(int year, int month);
    
    /**
     * Get returns in a specific month
     */
    long getReturnsInMonth(int year, int month);
    
    /**
     * Get total borrow transactions count
     */
    long getBorrowTransactionsCount();
    
    /**
     * Get total return transactions count
     */
    long getReturnTransactionsCount();
}
