package com.library.transactions;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
interface TransactionRepository extends JpaRepository<Transaction, Long> {
    
    List<Transaction> findByUserId(Long userId);
    
    List<Transaction> findByBookId(Long bookId);
    
    List<Transaction> findByStatus(Transaction.TransactionStatus status);
    
    List<Transaction> findByType(Transaction.TransactionType type);
    
    @Query("SELECT t FROM Transaction t WHERE t.userId = :userId AND t.status = :status")
    List<Transaction> findByUserIdAndStatus(@Param("userId") Long userId, @Param("status") Transaction.TransactionStatus status);
    
    @Query("SELECT t FROM Transaction t WHERE t.bookId = :bookId AND t.status = :status")
    List<Transaction> findByBookIdAndStatus(@Param("bookId") Long bookId, @Param("status") Transaction.TransactionStatus status);
    
    @Query("SELECT t FROM Transaction t WHERE t.dueDate < :date AND t.status = 'ACTIVE'")
    List<Transaction> findOverdueTransactions(@Param("date") LocalDateTime date);
    
    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.status = :status")
    long countByStatus(@Param("status") Transaction.TransactionStatus status);
    
    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.type = :type")
    long countByType(@Param("type") Transaction.TransactionType type);
    
    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.dueDate < :date AND t.status = 'ACTIVE'")
    long countOverdueTransactions(@Param("date") LocalDateTime date);
    
    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.createdAt >= :date")
    long countTransactionsAfter(@Param("date") LocalDateTime date);
    
    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.createdAt >= :startDate AND t.createdAt < :endDate")
    long countTransactionsBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.type = 'BORROW' AND t.createdAt >= :startDate AND t.createdAt < :endDate")
    long countBorrowsBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.returnedAt >= :startDate AND t.returnedAt < :endDate")
    long countReturnsBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT t FROM Transaction t WHERE t.userId = :userId AND t.bookId = :bookId AND t.status = 'ACTIVE'")
    Optional<Transaction> findActiveTransactionByUserAndBook(@Param("userId") Long userId, @Param("bookId") Long bookId);
}
