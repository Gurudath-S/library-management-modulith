package com.library.books;

import com.library.shared.events.BookAddedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookService implements BookModuleAPI {
    
    @Autowired
    private BookRepository bookRepository;
    
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Optional<Book> getBookByIdOptional(Long id) {
        return bookRepository.findById(id);
    }

    public Optional<Book> getBookByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn);
    }

    public List<Book> getBooksByCategory(String category) {
        return bookRepository.findByCategory(category);
    }

    public List<Book> searchBooks(String keyword) {
        return bookRepository.findByKeyword(keyword);
    }

    @Transactional
    public Book saveBook(Book book) {
        Book savedBook = bookRepository.save(book);
        
        // Publish event for new books
        if (book.getId() == null) {
            eventPublisher.publishEvent(new BookAddedEvent(
                savedBook.getId(),
                savedBook.getTitle(),
                savedBook.getAuthor(),
                savedBook.getIsbn(),
                savedBook.getCategory(),
                savedBook.getTotalCopies()
            ));
        }
        
        return savedBook;
    }

    @Transactional
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    public List<Book> getAvailableBooks() {
        return bookRepository.findByAvailableCopiesGreaterThan(0);
    }

    public List<String> getAllCategories() {
        return bookRepository.findAllCategories();
    }

    // Module API Implementation
    @Override
    public BookAnalytics getBookAnalytics() {
        long totalBooks = bookRepository.count();
        Long totalCopiesLong = bookRepository.getTotalBookCopies();
        Long availableCopiesLong = bookRepository.getAvailableBookCopies();
        
        long totalCopies = totalCopiesLong != null ? totalCopiesLong : 0;
        long availableCopies = availableCopiesLong != null ? availableCopiesLong : 0;
        long borrowedCopies = totalCopies - availableCopies;
        
        long outOfStockBooks = bookRepository.countOutOfStock();
        long lowStockBooks = bookRepository.countLowStock();
        
        // Get books by category
        Map<String, Long> booksByCategory = new HashMap<>();
        List<String> categories = bookRepository.findAllCategories();
        for (String category : categories) {
            long count = bookRepository.findByCategory(category).size();
            booksByCategory.put(category, count);
        }
        
        // For now, return empty popular books list
        // In a real implementation, this would be calculated from transaction history
        List<PopularBook> popularBooks = List.of();
        
        return new BookAnalytics(
            totalBooks,
            totalCopies,
            availableCopies,
            borrowedCopies,
            outOfStockBooks,
            lowStockBooks,
            booksByCategory,
            popularBooks
        );
    }
    
    @Override
    public BookInfo getBookInfo(Long bookId) {
        return bookRepository.findById(bookId)
            .map(book -> new BookInfo(
                book.getId(),
                book.getIsbn(),
                book.getTitle(),
                book.getAuthor(),
                book.getCategory(),
                book.getTotalCopies(),
                book.getAvailableCopies(),
                book.isAvailable()
            ))
            .orElse(null);
    }
    
    @Override
    public boolean isBookAvailable(Long bookId) {
        return bookRepository.findById(bookId)
            .map(Book::isAvailable)
            .orElse(false);
    }
    
    @Override
    @Transactional
    public void borrowBook(Long bookId) {
        Book book = bookRepository.findById(bookId)
            .orElseThrow(() -> new RuntimeException("Book not found"));
        
        book.borrowCopy();
        bookRepository.save(book);
    }
    
    @Override
    @Transactional
    public void returnBook(Long bookId) {
        Book book = bookRepository.findById(bookId)
            .orElseThrow(() -> new RuntimeException("Book not found"));
        
        book.returnCopy();
        bookRepository.save(book);
    }
    
    @Override
    public long getTotalBooks() {
        return bookRepository.count();
    }
    
    @Override
    public long getTotalCopies() {
        Long total = bookRepository.getTotalBookCopies();
        return total != null ? total : 0;
    }
    
    @Override
    public long getAvailableCopies() {
        Long available = bookRepository.getAvailableBookCopies();
        return available != null ? available : 0;
    }
    
    @Override
    public Map<String, Long> getBooksByCategory() {
        Map<String, Long> result = new HashMap<>();
        List<String> categories = bookRepository.findAllCategories();
        for (String category : categories) {
            long count = bookRepository.findByCategory(category).size();
            result.put(category, count);
        }
        return result;
    }
    
    @Override
    public long getTotalBooksCount() {
        return bookRepository.count();
    }
    
    @Override
    public long getTotalCopiesCount() {
        Long total = bookRepository.getTotalBookCopies();
        return total != null ? total : 0;
    }
    
    @Override
    public long getAvailableCopiesCount() {
        Long available = bookRepository.getAvailableBookCopies();
        return available != null ? available : 0;
    }
    
    @Override
    public long getOutOfStockBooksCount() {
        return bookRepository.countOutOfStock();
    }
    
    @Override
    public long getLowStockBooksCount(int threshold) {
        return bookRepository.countBooksWithCopiesLessThan(threshold);
    }
    
    @Override
    public Map<String, Long> getBookCountByCategory() {
        return getBooksByCategory(); // Same implementation
    }
    
    @Override
    public Book getBookById(Long bookId) {
        return bookRepository.findById(bookId).orElse(null);
    }
    
    @Override
    public List<Book> getBooksAddedAfter(java.time.LocalDateTime date) {
        return bookRepository.findBooksAddedAfter(date);
    }
    
    public Book createBook(Book book) {
        return bookRepository.save(book);
    }
}
