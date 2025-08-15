package com.library.transactions;

import com.library.books.BookModuleAPI;
import com.library.shared.events.BookBorrowedEvent;
import com.library.shared.events.BookReturnedEvent;
import com.library.users.UserModuleAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService implements TransactionModuleAPI {
    
    @Autowired
    private TransactionRepository transactionRepository;
    
    @Autowired
    private BookModuleAPI bookModuleAPI;
    
    @Autowired
    private UserModuleAPI userModuleAPI;
    
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public List<Transaction> getUserTransactionsEntity(Long userId) {
        return transactionRepository.findByUserId(userId);
    }

    public List<Transaction> getBookTransactionsEntity(Long bookId) {
        return transactionRepository.findByBookId(bookId);
    }

    public List<Transaction> getOverdueTransactionsEntity() {
        return transactionRepository.findOverdueTransactions(LocalDateTime.now());
    }

    @Transactional
    public Transaction borrowBookEntity(Long userId, Long bookId) {
        // Check if user exists
        if (!userModuleAPI.userExists(userId)) {
            throw new RuntimeException("User not found");
        }
        
        // Check if book is available
        if (!bookModuleAPI.isBookAvailable(bookId)) {
            throw new RuntimeException("Book is not available for borrowing");
        }
        
        // Check if user already has this book
        if (transactionRepository.findActiveTransactionByUserAndBook(userId, bookId).isPresent()) {
            throw new RuntimeException("User already has this book borrowed");
        }
        
        // Create transaction
        Transaction transaction = new Transaction(userId, bookId, Transaction.TransactionType.BORROW);
        Transaction savedTransaction = transactionRepository.save(transaction);
        
        // Update book availability through module API
        bookModuleAPI.borrowBook(bookId);
        
        // Publish event
        var userInfo = userModuleAPI.getUserInfo(userId);
        var bookInfo = bookModuleAPI.getBookInfo(bookId);
        
        if (userInfo != null && bookInfo != null) {
            eventPublisher.publishEvent(new BookBorrowedEvent(
                savedTransaction.getId(),
                bookId,
                userId,
                bookInfo.title(),
                userInfo.username()
            ));
        }
        
        return savedTransaction;
    }

    @Transactional
    public Transaction returnBookEntity(Long userId, Long bookId) {
        Transaction transaction = transactionRepository.findActiveTransactionByUserAndBook(userId, bookId)
            .orElseThrow(() -> new RuntimeException("No active borrowing transaction found"));
        
        boolean wasOverdue = transaction.isOverdue();
        transaction.markAsReturned();
        Transaction savedTransaction = transactionRepository.save(transaction);
        
        // Update book availability through module API
        bookModuleAPI.returnBook(bookId);
        
        // Publish event
        var userInfo = userModuleAPI.getUserInfo(userId);
        var bookInfo = bookModuleAPI.getBookInfo(bookId);
        
        if (userInfo != null && bookInfo != null) {
            eventPublisher.publishEvent(new BookReturnedEvent(
                savedTransaction.getId(),
                bookId,
                userId,
                bookInfo.title(),
                userInfo.username(),
                wasOverdue
            ));
        }
        
        return savedTransaction;
    }

    // Module API Implementation
    @Override
    public TransactionAnalytics getTransactionAnalytics() {
        long totalTransactions = transactionRepository.count();
        long activeTransactions = transactionRepository.countByStatus(Transaction.TransactionStatus.ACTIVE);
        long completedTransactions = transactionRepository.countByStatus(Transaction.TransactionStatus.COMPLETED);
        long overdueTransactions = transactionRepository.countOverdueTransactions(LocalDateTime.now());
        
        LocalDateTime startOfMonth = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        long transactionsThisMonth = transactionRepository.countTransactionsAfter(startOfMonth);
        
        // Calculate average borrowing duration
        List<Transaction> completedTransactionsList = transactionRepository.findByStatus(Transaction.TransactionStatus.COMPLETED);
        double averageBorrowingDuration = completedTransactionsList.stream()
            .filter(t -> t.getBorrowedAt() != null && t.getReturnedAt() != null)
            .mapToLong(t -> Duration.between(t.getBorrowedAt(), t.getReturnedAt()).toDays())
            .average()
            .orElse(0.0);
        
        return new TransactionAnalytics(
            totalTransactions,
            activeTransactions,
            completedTransactions,
            overdueTransactions,
            transactionsThisMonth,
            averageBorrowingDuration
        );
    }
    
    @Override
    public TransactionInfo getTransactionInfo(Long transactionId) {
        return transactionRepository.findById(transactionId)
            .map(this::mapToTransactionInfo)
            .orElse(null);
    }
    
    @Override
    public List<TransactionInfo> getUserTransactions(Long userId) {
        return transactionRepository.findByUserId(userId).stream()
            .map(this::mapToTransactionInfo)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<TransactionInfo> getBookTransactions(Long bookId) {
        return transactionRepository.findByBookId(bookId).stream()
            .map(this::mapToTransactionInfo)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<TransactionInfo> getOverdueTransactions() {
        return transactionRepository.findOverdueTransactions(LocalDateTime.now()).stream()
            .map(this::mapToTransactionInfo)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public TransactionInfo borrowBook(Long userId, Long bookId) {
        Transaction transaction = borrowBookEntity(userId, bookId);
        return mapToTransactionInfo(transaction);
    }
    
    @Override
    @Transactional
    public TransactionInfo returnBook(Long userId, Long bookId) {
        Transaction transaction = returnBookEntity(userId, bookId);
        return mapToTransactionInfo(transaction);
    }
    
    @Override
    public long getTotalTransactions() {
        return transactionRepository.count();
    }
    
    @Override
    public long getActiveTransactions() {
        return transactionRepository.countByStatus(Transaction.TransactionStatus.ACTIVE);
    }
    
    @Override
    public long getOverdueTransactionsCount() {
        return transactionRepository.countOverdueTransactions(LocalDateTime.now());
    }
    
    @Override
    public long getTransactionsThisMonth() {
        LocalDateTime startOfMonth = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        return transactionRepository.countTransactionsAfter(startOfMonth);
    }
    
    @Override
    public long getTotalTransactionsCount() {
        return transactionRepository.count();
    }
    
    @Override
    public long getActiveTransactionsCount() {
        return transactionRepository.countByStatus(Transaction.TransactionStatus.ACTIVE);
    }
    
    @Override
    public long getCompletedTransactionsCount() {
        return transactionRepository.countByStatus(Transaction.TransactionStatus.COMPLETED);
    }
    
    @Override
    public long getTransactionsInMonth(int year, int month) {
        LocalDateTime startOfMonth = LocalDateTime.of(year, month, 1, 0, 0, 0);
        LocalDateTime endOfMonth = startOfMonth.plusMonths(1);
        return transactionRepository.countTransactionsBetween(startOfMonth, endOfMonth);
    }
    
    @Override
    public double getAverageBorrowingDuration() {
        List<Transaction> completedTransactions = transactionRepository.findByStatus(Transaction.TransactionStatus.COMPLETED);
        return completedTransactions.stream()
            .filter(t -> t.getBorrowedAt() != null && t.getReturnedAt() != null)
            .mapToLong(t -> Duration.between(t.getBorrowedAt(), t.getReturnedAt()).toDays())
            .average()
            .orElse(0.0);
    }
    
    @Override
    public java.util.Map<Long, Long> getBookBorrowCounts() {
        return transactionRepository.findAll().stream()
            .filter(t -> t.getType() == Transaction.TransactionType.BORROW)
            .collect(Collectors.groupingBy(
                Transaction::getBookId,
                Collectors.counting()
            ));
    }
    
    @Override
    public long getBorrowsInMonth(int year, int month) {
        LocalDateTime startOfMonth = LocalDateTime.of(year, month, 1, 0, 0, 0);
        LocalDateTime endOfMonth = startOfMonth.plusMonths(1);
        return transactionRepository.countBorrowsBetween(startOfMonth, endOfMonth);
    }
    
    @Override
    public long getReturnsInMonth(int year, int month) {
        LocalDateTime startOfMonth = LocalDateTime.of(year, month, 1, 0, 0, 0);
        LocalDateTime endOfMonth = startOfMonth.plusMonths(1);
        return transactionRepository.countReturnsBetween(startOfMonth, endOfMonth);
    }
    
    public boolean hasActiveTransaction(Long userId, Long bookId) {
        return transactionRepository.findActiveTransactionByUserAndBook(userId, bookId).isPresent();
    }
    
    private TransactionInfo mapToTransactionInfo(Transaction transaction) {
        return new TransactionInfo(
            transaction.getId(),
            transaction.getUserId(),
            transaction.getBookId(),
            transaction.getType().name(),
            transaction.getStatus().name(),
            transaction.getBorrowedAt(),
            transaction.getDueDate(),
            transaction.getReturnedAt(),
            transaction.isOverdue()
        );
    }
}
