package com.library.books;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
interface BookRepository extends JpaRepository<Book, Long> {
    
    Optional<Book> findByIsbn(String isbn);
    
    List<Book> findByCategory(String category);
    
    List<Book> findByAuthorContainingIgnoreCase(String author);
    
    List<Book> findByTitleContainingIgnoreCase(String title);
    
    @Query("SELECT b FROM Book b WHERE b.title LIKE %:keyword% OR b.author LIKE %:keyword% OR b.isbn LIKE %:keyword%")
    List<Book> findByKeyword(@Param("keyword") String keyword);
    
    List<Book> findByStatus(Book.BookStatus status);
    
    List<Book> findByAvailableCopiesGreaterThan(int copies);
    
    @Query("SELECT COUNT(b) FROM Book b WHERE b.status = :status")
    long countByStatus(@Param("status") Book.BookStatus status);
    
    @Query("SELECT COUNT(b) FROM Book b WHERE b.availableCopies = 0")
    long countOutOfStock();
    
    @Query("SELECT COUNT(b) FROM Book b WHERE b.availableCopies > 0 AND b.availableCopies <= 5")
    long countLowStock();
    
    @Query("SELECT DISTINCT b.category FROM Book b ORDER BY b.category")
    List<String> findAllCategories();
    
    @Query("SELECT SUM(b.totalCopies) FROM Book b")
    Long getTotalBookCopies();
    
    @Query("SELECT SUM(b.availableCopies) FROM Book b")
    Long getAvailableBookCopies();
    
    @Query("SELECT COUNT(b) FROM Book b WHERE b.availableCopies < :threshold")
    long countBooksWithCopiesLessThan(@Param("threshold") int threshold);
    
    @Query("SELECT b FROM Book b WHERE b.createdAt >= :date ORDER BY b.createdAt DESC")
    List<Book> findBooksAddedAfter(@Param("date") LocalDateTime date);
}
