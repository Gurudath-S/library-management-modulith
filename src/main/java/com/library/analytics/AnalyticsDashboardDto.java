package com.library.analytics;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class AnalyticsDashboardDto {
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime generatedAt;
    
    private UserAnalyticsDto userAnalytics;
    private BookAnalyticsDto bookAnalytics;
    private TransactionAnalyticsDto transactionAnalytics;
    private InventoryAnalyticsDto inventoryAnalytics;
    private SystemHealthDto systemHealth;
    
    // Constructors
    public AnalyticsDashboardDto() {
        this.generatedAt = LocalDateTime.now();
    }
    
    public AnalyticsDashboardDto(UserAnalyticsDto userAnalytics, 
                                BookAnalyticsDto bookAnalytics,
                                TransactionAnalyticsDto transactionAnalytics,
                                InventoryAnalyticsDto inventoryAnalytics,
                                SystemHealthDto systemHealth) {
        this();
        this.userAnalytics = userAnalytics;
        this.bookAnalytics = bookAnalytics;
        this.transactionAnalytics = transactionAnalytics;
        this.inventoryAnalytics = inventoryAnalytics;
        this.systemHealth = systemHealth;
    }
    
    // Getters and Setters
    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }
    
    public void setGeneratedAt(LocalDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }
    
    public UserAnalyticsDto getUserAnalytics() {
        return userAnalytics;
    }
    
    public void setUserAnalytics(UserAnalyticsDto userAnalytics) {
        this.userAnalytics = userAnalytics;
    }
    
    public BookAnalyticsDto getBookAnalytics() {
        return bookAnalytics;
    }
    
    public void setBookAnalytics(BookAnalyticsDto bookAnalytics) {
        this.bookAnalytics = bookAnalytics;
    }
    
    public TransactionAnalyticsDto getTransactionAnalytics() {
        return transactionAnalytics;
    }
    
    public void setTransactionAnalytics(TransactionAnalyticsDto transactionAnalytics) {
        this.transactionAnalytics = transactionAnalytics;
    }
    
    public InventoryAnalyticsDto getInventoryAnalytics() {
        return inventoryAnalytics;
    }
    
    public void setInventoryAnalytics(InventoryAnalyticsDto inventoryAnalytics) {
        this.inventoryAnalytics = inventoryAnalytics;
    }
    
    public SystemHealthDto getSystemHealth() {
        return systemHealth;
    }
    
    public void setSystemHealth(SystemHealthDto systemHealth) {
        this.systemHealth = systemHealth;
    }
    
    // Nested DTOs
    public static class UserAnalyticsDto {
        private long totalUsers;
        private long activeUsers;
        private long newUsersThisMonth;
        private long adminCount;
        private long librarianCount;
        private long userCount;
        private Map<String, Long> usersByRole;
        private List<UserGrowthDto> userGrowth;
        
        // Constructors, getters and setters
        public UserAnalyticsDto() {}
        
        public UserAnalyticsDto(long totalUsers, long activeUsers, long newUsersThisMonth,
                               long adminCount, long librarianCount, long userCount) {
            this.totalUsers = totalUsers;
            this.activeUsers = activeUsers;
            this.newUsersThisMonth = newUsersThisMonth;
            this.adminCount = adminCount;
            this.librarianCount = librarianCount;
            this.userCount = userCount;
        }
        
        // Getters and Setters
        public long getTotalUsers() { return totalUsers; }
        public void setTotalUsers(long totalUsers) { this.totalUsers = totalUsers; }
        
        public long getActiveUsers() { return activeUsers; }
        public void setActiveUsers(long activeUsers) { this.activeUsers = activeUsers; }
        
        public long getNewUsersThisMonth() { return newUsersThisMonth; }
        public void setNewUsersThisMonth(long newUsersThisMonth) { this.newUsersThisMonth = newUsersThisMonth; }
        
        public long getAdminCount() { return adminCount; }
        public void setAdminCount(long adminCount) { this.adminCount = adminCount; }
        
        public long getLibrarianCount() { return librarianCount; }
        public void setLibrarianCount(long librarianCount) { this.librarianCount = librarianCount; }
        
        public long getUserCount() { return userCount; }
        public void setUserCount(long userCount) { this.userCount = userCount; }
        
        public Map<String, Long> getUsersByRole() { return usersByRole; }
        public void setUsersByRole(Map<String, Long> usersByRole) { this.usersByRole = usersByRole; }
        
        public List<UserGrowthDto> getUserGrowth() { return userGrowth; }
        public void setUserGrowth(List<UserGrowthDto> userGrowth) { this.userGrowth = userGrowth; }
    }
    
    public static class BookAnalyticsDto {
        private long totalBooks;
        private long totalCopies;
        private long availableCopies;
        private long borrowedCopies;
        private long outOfStockBooks;
        private long lowStockBooks;
        private Map<String, Long> booksByCategory;
        private List<PopularBookDto> popularBooks;
        private List<RecentBookDto> recentBooks;
        
        // Constructors
        public BookAnalyticsDto() {}
        
        public BookAnalyticsDto(long totalBooks, long totalCopies, long availableCopies,
                               long borrowedCopies, long outOfStockBooks, long lowStockBooks) {
            this.totalBooks = totalBooks;
            this.totalCopies = totalCopies;
            this.availableCopies = availableCopies;
            this.borrowedCopies = borrowedCopies;
            this.outOfStockBooks = outOfStockBooks;
            this.lowStockBooks = lowStockBooks;
        }
        
        // Getters and Setters
        public long getTotalBooks() { return totalBooks; }
        public void setTotalBooks(long totalBooks) { this.totalBooks = totalBooks; }
        
        public long getTotalCopies() { return totalCopies; }
        public void setTotalCopies(long totalCopies) { this.totalCopies = totalCopies; }
        
        public long getAvailableCopies() { return availableCopies; }
        public void setAvailableCopies(long availableCopies) { this.availableCopies = availableCopies; }
        
        public long getBorrowedCopies() { return borrowedCopies; }
        public void setBorrowedCopies(long borrowedCopies) { this.borrowedCopies = borrowedCopies; }
        
        public long getOutOfStockBooks() { return outOfStockBooks; }
        public void setOutOfStockBooks(long outOfStockBooks) { this.outOfStockBooks = outOfStockBooks; }
        
        public long getLowStockBooks() { return lowStockBooks; }
        public void setLowStockBooks(long lowStockBooks) { this.lowStockBooks = lowStockBooks; }
        
        public Map<String, Long> getBooksByCategory() { return booksByCategory; }
        public void setBooksByCategory(Map<String, Long> booksByCategory) { this.booksByCategory = booksByCategory; }
        
        public List<PopularBookDto> getPopularBooks() { return popularBooks; }
        public void setPopularBooks(List<PopularBookDto> popularBooks) { this.popularBooks = popularBooks; }
        
        public List<RecentBookDto> getRecentBooks() { return recentBooks; }
        public void setRecentBooks(List<RecentBookDto> recentBooks) { this.recentBooks = recentBooks; }
    }
    
    public static class TransactionAnalyticsDto {
        private long totalTransactions;
        private long activeTransactions;
        private long completedTransactions;
        private long overdueTransactions;
        private long transactionsThisMonth;
        private double averageBorrowingDuration;
        private List<BorrowingPatternDto> borrowingPatterns;
        
        // Constructors
        public TransactionAnalyticsDto() {}
        
        public TransactionAnalyticsDto(long totalTransactions, long activeTransactions,
                                     long completedTransactions, long overdueTransactions,
                                     long transactionsThisMonth, double averageBorrowingDuration) {
            this.totalTransactions = totalTransactions;
            this.activeTransactions = activeTransactions;
            this.completedTransactions = completedTransactions;
            this.overdueTransactions = overdueTransactions;
            this.transactionsThisMonth = transactionsThisMonth;
            this.averageBorrowingDuration = averageBorrowingDuration;
        }
        
        // Getters and Setters
        public long getTotalTransactions() { return totalTransactions; }
        public void setTotalTransactions(long totalTransactions) { this.totalTransactions = totalTransactions; }
        
        public long getActiveTransactions() { return activeTransactions; }
        public void setActiveTransactions(long activeTransactions) { this.activeTransactions = activeTransactions; }
        
        public long getCompletedTransactions() { return completedTransactions; }
        public void setCompletedTransactions(long completedTransactions) { this.completedTransactions = completedTransactions; }
        
        public long getOverdueTransactions() { return overdueTransactions; }
        public void setOverdueTransactions(long overdueTransactions) { this.overdueTransactions = overdueTransactions; }
        
        public long getTransactionsThisMonth() { return transactionsThisMonth; }
        public void setTransactionsThisMonth(long transactionsThisMonth) { this.transactionsThisMonth = transactionsThisMonth; }
        
        public double getAverageBorrowingDuration() { return averageBorrowingDuration; }
        public void setAverageBorrowingDuration(double averageBorrowingDuration) { this.averageBorrowingDuration = averageBorrowingDuration; }
        
        public List<BorrowingPatternDto> getBorrowingPatterns() { return borrowingPatterns; }
        public void setBorrowingPatterns(List<BorrowingPatternDto> borrowingPatterns) { this.borrowingPatterns = borrowingPatterns; }
    }
    
    public static class InventoryAnalyticsDto {
        private long totalCopies;
        private long availableCopies;
        private long borrowedCopies;
        private double utilizationRate;
        private long lowStockAlerts;
        private long outOfStockItems;
        
        // Constructors
        public InventoryAnalyticsDto() {}
        
        public InventoryAnalyticsDto(long totalCopies, long availableCopies, long borrowedCopies,
                                   double utilizationRate, long lowStockAlerts, long outOfStockItems) {
            this.totalCopies = totalCopies;
            this.availableCopies = availableCopies;
            this.borrowedCopies = borrowedCopies;
            this.utilizationRate = utilizationRate;
            this.lowStockAlerts = lowStockAlerts;
            this.outOfStockItems = outOfStockItems;
        }
        
        // Getters and Setters
        public long getTotalCopies() { return totalCopies; }
        public void setTotalCopies(long totalCopies) { this.totalCopies = totalCopies; }
        
        public long getAvailableCopies() { return availableCopies; }
        public void setAvailableCopies(long availableCopies) { this.availableCopies = availableCopies; }
        
        public long getBorrowedCopies() { return borrowedCopies; }
        public void setBorrowedCopies(long borrowedCopies) { this.borrowedCopies = borrowedCopies; }
        
        public double getUtilizationRate() { return utilizationRate; }
        public void setUtilizationRate(double utilizationRate) { this.utilizationRate = utilizationRate; }
        
        public long getLowStockAlerts() { return lowStockAlerts; }
        public void setLowStockAlerts(long lowStockAlerts) { this.lowStockAlerts = lowStockAlerts; }
        
        public long getOutOfStockItems() { return outOfStockItems; }
        public void setOutOfStockItems(long outOfStockItems) { this.outOfStockItems = outOfStockItems; }
    }
    
    public static class SystemHealthDto {
        private String status;
        private double cpuUsage;
        private double memoryUsage;
        private long uptime;
        private int activeConnections;
        
        // Constructors
        public SystemHealthDto() {}
        
        public SystemHealthDto(String status, double cpuUsage, double memoryUsage, long uptime, int activeConnections) {
            this.status = status;
            this.cpuUsage = cpuUsage;
            this.memoryUsage = memoryUsage;
            this.uptime = uptime;
            this.activeConnections = activeConnections;
        }
        
        // Getters and Setters
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        
        public double getCpuUsage() { return cpuUsage; }
        public void setCpuUsage(double cpuUsage) { this.cpuUsage = cpuUsage; }
        
        public double getMemoryUsage() { return memoryUsage; }
        public void setMemoryUsage(double memoryUsage) { this.memoryUsage = memoryUsage; }
        
        public long getUptime() { return uptime; }
        public void setUptime(long uptime) { this.uptime = uptime; }
        
        public int getActiveConnections() { return activeConnections; }
        public void setActiveConnections(int activeConnections) { this.activeConnections = activeConnections; }
    }
    
    // Supporting DTOs
    public static class UserGrowthDto {
        private String period;
        private long userCount;
        
        public UserGrowthDto() {}
        public UserGrowthDto(String period, long userCount) {
            this.period = period;
            this.userCount = userCount;
        }
        
        public String getPeriod() { return period; }
        public void setPeriod(String period) { this.period = period; }
        
        public long getUserCount() { return userCount; }
        public void setUserCount(long userCount) { this.userCount = userCount; }
    }
    
    public static class PopularBookDto {
        private Long bookId;
        private String title;
        private String author;
        private long borrowCount;
        
        public PopularBookDto() {}
        public PopularBookDto(Long bookId, String title, String author, long borrowCount) {
            this.bookId = bookId;
            this.title = title;
            this.author = author;
            this.borrowCount = borrowCount;
        }
        
        public Long getBookId() { return bookId; }
        public void setBookId(Long bookId) { this.bookId = bookId; }
        
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        
        public String getAuthor() { return author; }
        public void setAuthor(String author) { this.author = author; }
        
        public long getBorrowCount() { return borrowCount; }
        public void setBorrowCount(long borrowCount) { this.borrowCount = borrowCount; }
    }
    
    public static class RecentBookDto {
        private Long bookId;
        private String title;
        private String author;
        private LocalDateTime addedDate;
        
        public RecentBookDto() {}
        public RecentBookDto(Long bookId, String title, String author, LocalDateTime addedDate) {
            this.bookId = bookId;
            this.title = title;
            this.author = author;
            this.addedDate = addedDate;
        }
        
        public Long getBookId() { return bookId; }
        public void setBookId(Long bookId) { this.bookId = bookId; }
        
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        
        public String getAuthor() { return author; }
        public void setAuthor(String author) { this.author = author; }
        
        public LocalDateTime getAddedDate() { return addedDate; }
        public void setAddedDate(LocalDateTime addedDate) { this.addedDate = addedDate; }
    }
    
    public static class BorrowingPatternDto {
        private String period;
        private long borrowCount;
        private long returnCount;
        
        public BorrowingPatternDto() {}
        public BorrowingPatternDto(String period, long borrowCount, long returnCount) {
            this.period = period;
            this.borrowCount = borrowCount;
            this.returnCount = returnCount;
        }
        
        public String getPeriod() { return period; }
        public void setPeriod(String period) { this.period = period; }
        
        public long getBorrowCount() { return borrowCount; }
        public void setBorrowCount(long borrowCount) { this.borrowCount = borrowCount; }
        
        public long getReturnCount() { return returnCount; }
        public void setReturnCount(long returnCount) { this.returnCount = returnCount; }
    }
}
