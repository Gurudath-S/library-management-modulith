package com.library.analytics;

import com.library.books.BookModuleAPI;
import com.library.transactions.TransactionModuleAPI;
import com.library.users.UserModuleAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.management.ManagementFactory;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnalyticsService implements AnalyticsModuleAPI {
    
    @Autowired
    private UserModuleAPI userModuleAPI;
    
    @Autowired
    private BookModuleAPI bookModuleAPI;
    
    @Autowired
    private TransactionModuleAPI transactionModuleAPI;
    
    @Override
    public AnalyticsDashboardDto generateDashboard() {
        AnalyticsDashboardDto.UserAnalyticsDto userAnalytics = generateUserAnalytics();
        AnalyticsDashboardDto.BookAnalyticsDto bookAnalytics = generateBookAnalytics();
        AnalyticsDashboardDto.TransactionAnalyticsDto transactionAnalytics = generateTransactionAnalytics();
        AnalyticsDashboardDto.InventoryAnalyticsDto inventoryAnalytics = generateInventoryAnalytics();
        AnalyticsDashboardDto.SystemHealthDto systemHealth = generateSystemHealth();
        
        return new AnalyticsDashboardDto(userAnalytics, bookAnalytics, transactionAnalytics, 
                                       inventoryAnalytics, systemHealth);
    }
    
    @Override
    public AnalyticsDashboardDto.UserAnalyticsDto generateUserAnalytics() {
        long totalUsers = userModuleAPI.getTotalUsersCount();
        long adminCount = userModuleAPI.getUserCountByRole("ADMIN");
        long librarianCount = userModuleAPI.getUserCountByRole("LIBRARIAN");
        long userCount = userModuleAPI.getUserCountByRole("USER");
        
        // Active users (simplified - could be enhanced with last login tracking)
        long activeUsers = totalUsers;
        
        // New users this month
        YearMonth currentMonth = YearMonth.now();
        long newUsersThisMonth = userModuleAPI.getUsersCreatedInMonth(currentMonth.getYear(), currentMonth.getMonthValue());
        
        AnalyticsDashboardDto.UserAnalyticsDto userAnalytics = new AnalyticsDashboardDto.UserAnalyticsDto(
            totalUsers, activeUsers, newUsersThisMonth, adminCount, librarianCount, userCount
        );
        
        // Set users by role map
        Map<String, Long> usersByRole = Map.of(
            "ADMIN", adminCount,
            "LIBRARIAN", librarianCount,
            "USER", userCount
        );
        userAnalytics.setUsersByRole(usersByRole);
        
        // Generate user growth data (last 6 months)
        List<AnalyticsDashboardDto.UserGrowthDto> userGrowth = generateUserGrowthData();
        userAnalytics.setUserGrowth(userGrowth);
        
        return userAnalytics;
    }
    
    @Override
    public AnalyticsDashboardDto.BookAnalyticsDto generateBookAnalytics() {
        long totalBooks = bookModuleAPI.getTotalBooksCount();
        long totalCopies = bookModuleAPI.getTotalCopiesCount();
        long availableCopies = bookModuleAPI.getAvailableCopiesCount();
        long borrowedCopies = totalCopies - availableCopies;
        long outOfStockBooks = bookModuleAPI.getOutOfStockBooksCount();
        long lowStockBooks = bookModuleAPI.getLowStockBooksCount(5); // Low stock threshold: 5
        
        AnalyticsDashboardDto.BookAnalyticsDto bookAnalytics = new AnalyticsDashboardDto.BookAnalyticsDto(
            totalBooks, totalCopies, availableCopies, borrowedCopies, outOfStockBooks, lowStockBooks
        );
        
        // Set books by category
        Map<String, Long> booksByCategory = bookModuleAPI.getBookCountByCategory();
        bookAnalytics.setBooksByCategory(booksByCategory);
        
        // Popular books (most borrowed)
        List<AnalyticsDashboardDto.PopularBookDto> popularBooks = generatePopularBooks();
        bookAnalytics.setPopularBooks(popularBooks);
        
        // Recent books (recently added)
        List<AnalyticsDashboardDto.RecentBookDto> recentBooks = generateRecentBooks();
        bookAnalytics.setRecentBooks(recentBooks);
        
        return bookAnalytics;
    }
    
    @Override
    public AnalyticsDashboardDto.TransactionAnalyticsDto generateTransactionAnalytics() {
        long totalTransactions = transactionModuleAPI.getTotalTransactionsCount();
        long activeTransactions = transactionModuleAPI.getActiveTransactionsCount();
        long completedTransactions = transactionModuleAPI.getCompletedTransactionsCount();
        long overdueTransactions = transactionModuleAPI.getOverdueTransactionsCount();
        
        // Transactions this month
        YearMonth currentMonth = YearMonth.now();
        long transactionsThisMonth = transactionModuleAPI.getTransactionsInMonth(currentMonth.getYear(), currentMonth.getMonthValue());
        
        // Average borrowing duration
        double averageBorrowingDuration = transactionModuleAPI.getAverageBorrowingDuration();
        
        AnalyticsDashboardDto.TransactionAnalyticsDto transactionAnalytics = new AnalyticsDashboardDto.TransactionAnalyticsDto(
            totalTransactions, activeTransactions, completedTransactions, overdueTransactions,
            transactionsThisMonth, averageBorrowingDuration
        );
        
        // Generate borrowing patterns (last 6 months)
        List<AnalyticsDashboardDto.BorrowingPatternDto> borrowingPatterns = generateBorrowingPatterns();
        transactionAnalytics.setBorrowingPatterns(borrowingPatterns);
        
        return transactionAnalytics;
    }
    
    @Override
    public AnalyticsDashboardDto.InventoryAnalyticsDto generateInventoryAnalytics() {
        long totalCopies = bookModuleAPI.getTotalCopiesCount();
        long availableCopies = bookModuleAPI.getAvailableCopiesCount();
        long borrowedCopies = totalCopies - availableCopies;
        double utilizationRate = totalCopies > 0 ? (double) borrowedCopies / totalCopies * 100 : 0;
        long lowStockAlerts = bookModuleAPI.getLowStockBooksCount(5);
        long outOfStockItems = bookModuleAPI.getOutOfStockBooksCount();
        
        return new AnalyticsDashboardDto.InventoryAnalyticsDto(
            totalCopies, availableCopies, borrowedCopies, utilizationRate, lowStockAlerts, outOfStockItems
        );
    }
    
    @Override
    public AnalyticsDashboardDto.SystemHealthDto generateSystemHealth() {
        // System health metrics
        Runtime runtime = Runtime.getRuntime();
        long maxMemory = runtime.maxMemory();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = totalMemory - freeMemory;
        
        double memoryUsage = (double) usedMemory / maxMemory * 100;
        
        // CPU usage (simplified - using a different approach)
        com.sun.management.OperatingSystemMXBean osBean = 
            (com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        double cpuUsage = osBean.getProcessCpuLoad() * 100;
        if (cpuUsage < 0) cpuUsage = 0; // getProcessCpuLoad() can return -1
        
        // Uptime
        long uptime = ManagementFactory.getRuntimeMXBean().getUptime();
        
        // System status
        String status = "HEALTHY";
        if (memoryUsage > 90 || cpuUsage > 90) {
            status = "WARNING";
        }
        if (memoryUsage > 95 || cpuUsage > 95) {
            status = "CRITICAL";
        }
        
        // Active connections (simplified)
        int activeConnections = 1; // Current connection
        
        return new AnalyticsDashboardDto.SystemHealthDto(status, cpuUsage, memoryUsage, uptime, activeConnections);
    }
    
    private List<AnalyticsDashboardDto.UserGrowthDto> generateUserGrowthData() {
        List<AnalyticsDashboardDto.UserGrowthDto> growthData = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        
        for (int i = 5; i >= 0; i--) {
            YearMonth month = YearMonth.from(now.minusMonths(i));
            long userCount = userModuleAPI.getUsersCreatedInMonth(month.getYear(), month.getMonthValue());
            growthData.add(new AnalyticsDashboardDto.UserGrowthDto(
                month.toString(), userCount
            ));
        }
        
        return growthData;
    }
    
    private List<AnalyticsDashboardDto.PopularBookDto> generatePopularBooks() {
        // Get top 10 most borrowed books
        Map<Long, Long> borrowCounts = transactionModuleAPI.getBookBorrowCounts();
        
        return borrowCounts.entrySet().stream()
            .sorted(Map.Entry.<Long, Long>comparingByValue().reversed())
            .limit(10)
            .map(entry -> {
                Long bookId = entry.getKey();
                var book = bookModuleAPI.getBookById(bookId);
                return new AnalyticsDashboardDto.PopularBookDto(
                    bookId, 
                    book != null ? book.getTitle() : "Unknown",
                    book != null ? book.getAuthor() : "Unknown",
                    entry.getValue()
                );
            })
            .collect(Collectors.toList());
    }
    
    private List<AnalyticsDashboardDto.RecentBookDto> generateRecentBooks() {
        // Get recently added books (last 30 days)
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        var recentBooks = bookModuleAPI.getBooksAddedAfter(thirtyDaysAgo);
        
        return recentBooks.stream()
            .limit(10)
            .map(book -> new AnalyticsDashboardDto.RecentBookDto(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getCreatedAt()
            ))
            .collect(Collectors.toList());
    }
    
    private List<AnalyticsDashboardDto.BorrowingPatternDto> generateBorrowingPatterns() {
        List<AnalyticsDashboardDto.BorrowingPatternDto> patterns = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        
        for (int i = 5; i >= 0; i--) {
            YearMonth month = YearMonth.from(now.minusMonths(i));
            long borrowCount = transactionModuleAPI.getBorrowsInMonth(month.getYear(), month.getMonthValue());
            long returnCount = transactionModuleAPI.getReturnsInMonth(month.getYear(), month.getMonthValue());
            
            patterns.add(new AnalyticsDashboardDto.BorrowingPatternDto(
                month.toString(), borrowCount, returnCount
            ));
        }
        
        return patterns;
    }
}
