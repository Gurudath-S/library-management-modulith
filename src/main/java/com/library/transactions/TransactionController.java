package com.library.transactions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.library.users.User;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "*")
public class TransactionController {
    
    @Autowired
    private TransactionService transactionService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('LIBRARIAN')")
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        List<Transaction> transactions = transactionService.getAllTransactions();
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('LIBRARIAN') or (hasRole('USER') and #userId == authentication.principal.id)")
    public ResponseEntity<List<Transaction>> getUserTransactions(@PathVariable Long userId) {
        List<Transaction> transactions = transactionService.getUserTransactionsEntity(userId);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/my")
    public ResponseEntity<List<Transaction>> getMyTransactions() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        List<Transaction> transactions = transactionService.getUserTransactionsEntity(currentUser.getId());
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/book/{bookId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('LIBRARIAN')")
    public ResponseEntity<List<Transaction>> getBookTransactions(@PathVariable Long bookId) {
        List<Transaction> transactions = transactionService.getBookTransactionsEntity(bookId);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/overdue")
    @PreAuthorize("hasRole('ADMIN') or hasRole('LIBRARIAN')")
    public ResponseEntity<List<Transaction>> getOverdueTransactions() {
        List<Transaction> transactions = transactionService.getOverdueTransactionsEntity();
        return ResponseEntity.ok(transactions);
    }

    @PostMapping("/borrow/{bookId}")
    public ResponseEntity<?> borrowBook(@PathVariable Long bookId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User currentUser = (User) authentication.getPrincipal();
            
            Transaction transaction = transactionService.borrowBookEntity(currentUser.getId(), bookId);
            return ResponseEntity.ok(transaction);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/return/{bookId}")
    public ResponseEntity<?> returnBook(@PathVariable Long bookId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User currentUser = (User) authentication.getPrincipal();
            
            Transaction transaction = transactionService.returnBookEntity(currentUser.getId(), bookId);
            return ResponseEntity.ok(transaction);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/admin/borrow")
    @PreAuthorize("hasRole('ADMIN') or hasRole('LIBRARIAN')")
    public ResponseEntity<?> adminBorrowBook(@RequestParam Long userId, @RequestParam Long bookId) {
        try {
            Transaction transaction = transactionService.borrowBookEntity(userId, bookId);
            return ResponseEntity.ok(transaction);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/admin/return")
    @PreAuthorize("hasRole('ADMIN') or hasRole('LIBRARIAN')")
    public ResponseEntity<?> adminReturnBook(@RequestParam Long userId, @RequestParam Long bookId) {
        try {
            Transaction transaction = transactionService.returnBookEntity(userId, bookId);
            return ResponseEntity.ok(transaction);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
