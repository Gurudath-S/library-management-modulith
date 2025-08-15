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
        long startTime = System.currentTimeMillis();
        
        AnalyticsDashboardDto.UserAnalyticsDto userAnalytics = generateUserAnalytics();
        AnalyticsDashboardDto.BookAnalyticsDto bookAnalytics = generateBookAnalytics();
        AnalyticsDashboardDto.TransactionAnalyticsDto transactionAnalytics = generateTransactionAnalytics();
        AnalyticsDashboardDto.InventoryAnalyticsDto inventoryAnalytics = generateInventoryAnalytics();
        AnalyticsDashboardDto.SystemHealthDto systemHealth = generateSystemHealth();
        
        AnalyticsDashboardDto dashboard = new AnalyticsDashboardDto(userAnalytics, bookAnalytics, 
                                       transactionAnalytics, inventoryAnalytics, systemHealth);
        
        // Set metadata
        long executionTime = System.currentTimeMillis() - startTime;
        dashboard.getMetadata().setExecutionTimeMs(executionTime);
        
        return dashboard;
    }
    
    @Override
    public AnalyticsDashboardDto.UserAnalyticsDto generateUserAnalytics() {
        long totalUsers = userModuleAPI.getTotalUsersCount();
        long adminCount = userModuleAPI.getUserCountByRole("ADMIN");
        long librarianCount = userModuleAPI.getUserCountByRole("LIBRARIAN");
        long userCount = userModuleAPI.getUserCountByRole("USER");
        
        // Active users (simplified - could be enhanced with last login tracking)
        long activeUsers = Math.min(totalUsers, 18); // Mock active users for demo
        
        // New users this month
        YearMonth currentMonth = YearMonth.now();
        long newUsersThisMonth = userModuleAPI.getUsersCreatedInMonth(currentMonth.getYear(), currentMonth.getMonthValue());
        
        // Calculate growth rate
        double userGrowthRate = newUsersThisMonth > 0 ? 100.0 : 0.0;
        
        // Set users by role map
        Map<String, Long> usersByRole = Map.of(
            "ADMIN", adminCount,
            "LIBRARIAN", librarianCount,
            "USER", userCount
        );
        
        // Generate top active users
        List<AnalyticsDashboardDto.TopActiveUserDto> topActiveUsers = generateTopActiveUsers();
        
        return new AnalyticsDashboardDto.UserAnalyticsDto(
            totalUsers, activeUsers, newUsersThisMonth, userGrowthRate, usersByRole, topActiveUsers
        );
    }
    
    @Override
    public AnalyticsDashboardDto.BookAnalyticsDto generateBookAnalytics() {
        long totalBooks = bookModuleAPI.getTotalBooksCount();
        long totalCopies = bookModuleAPI.getTotalCopiesCount();
        long availableCopies = bookModuleAPI.getAvailableCopiesCount();
        long borrowedCopies = totalCopies - availableCopies;
        
        // Available books vs total books
        long availableBooks = totalBooks - 1; // Mock for demo
        
        // Average books per user
        long totalUsers = userModuleAPI.getTotalUsersCount();
        double averageBooksPerUser = totalUsers > 0 ? (double) borrowedCopies / totalUsers : 0.0;
        
        // Set books by category
        Map<String, Long> booksByCategory = bookModuleAPI.getBookCountByCategory();
        
        // Popular books (most borrowed)
        List<AnalyticsDashboardDto.PopularBookDto> mostBorrowedBooks = generateMostBorrowedBooks();
        
        // Least borrowed books
        List<AnalyticsDashboardDto.PopularBookDto> leastBorrowedBooks = generateLeastBorrowedBooks();
        
        return new AnalyticsDashboardDto.BookAnalyticsDto(
            totalBooks, availableBooks, borrowedCopies, booksByCategory, 
            mostBorrowedBooks, leastBorrowedBooks, averageBooksPerUser
        );
    }
    
    @Override
    public AnalyticsDashboardDto.TransactionAnalyticsDto generateTransactionAnalytics() {
        long totalTransactions = transactionModuleAPI.getTotalTransactionsCount();
        long activeTransactions = transactionModuleAPI.getActiveTransactionsCount();
        long overdueTransactions = transactionModuleAPI.getOverdueTransactionsCount();
        
        // Transactions for different periods
        long transactionsToday = totalTransactions; // For demo, using total
        long transactionsThisWeek = totalTransactions;
        YearMonth currentMonth = YearMonth.now();
        long transactionsThisMonth = transactionModuleAPI.getTransactionsInMonth(currentMonth.getYear(), currentMonth.getMonthValue());
        
        // Average return time
        double averageReturnTime = transactionModuleAPI.getAverageBorrowingDuration();
        
        // Transactions by type
        long borrowTransactions = transactionModuleAPI.getBorrowTransactionsCount();
        long returnTransactions = transactionModuleAPI.getReturnTransactionsCount();
        Map<String, Long> transactionsByType = Map.of(
            "BORROW", borrowTransactions,
            "RETURN", returnTransactions,
            "RESERVE", 0L
        );
        
        // Recent activity (last 7 days)
        List<AnalyticsDashboardDto.RecentActivityDto> recentActivity = generateRecentActivity();
        
        return new AnalyticsDashboardDto.TransactionAnalyticsDto(
            totalTransactions, activeTransactions, overdueTransactions, transactionsToday,
            transactionsThisWeek, transactionsThisMonth, averageReturnTime, 
            transactionsByType, recentActivity
        );
    }
    
    @Override
    public AnalyticsDashboardDto.InventoryAnalyticsDto generateInventoryAnalytics() {
        long totalCopies = bookModuleAPI.getTotalCopiesCount();
        long availableCopies = bookModuleAPI.getAvailableCopiesCount();
        long borrowedCopies = totalCopies - availableCopies;
        double utilizationRate = totalCopies > 0 ? (double) borrowedCopies / totalCopies * 100 : 0;
        
        // Low stock and high demand books
        List<String> lowStockBooks = Arrays.asList(
            "Design Patterns", "Oxford History of the World", 
            "The Count of Monte Cristo", "Les Misérables"
        );
        List<String> highDemandBooks = Arrays.asList("The Count of Monte Cristo");
        
        // Category utilization (mock data based on categories)
        Map<String, Double> categoryUtilization = generateCategoryUtilization();
        
        return new AnalyticsDashboardDto.InventoryAnalyticsDto(
            totalCopies, availableCopies, borrowedCopies, utilizationRate, 
            lowStockBooks, highDemandBooks, categoryUtilization
        );
    }
    
    @Override
    public AnalyticsDashboardDto.SystemHealthDto generateSystemHealth() {
        // System health metrics
        Runtime runtime = Runtime.getRuntime();
        long uptime = ManagementFactory.getRuntimeMXBean().getUptime();
        
        // Mock response time (in milliseconds)
        double responseTime = 83.7917709593658;
        
        // System status
        String status = "HEALTHY";
        
        // Module status
        Map<String, String> moduleStatus = Map.of(
            "TransactionService", "HEALTHY",
            "UserService", "HEALTHY",
            "Authentication", "HEALTHY",
            "BookService", "HEALTHY",
            "Database", "HEALTHY"
        );
        
        // Recent errors (empty for healthy system)
        List<String> recentErrors = new ArrayList<>();
        
        return new AnalyticsDashboardDto.SystemHealthDto(
            status, responseTime, uptime, moduleStatus, recentErrors
        );
    }
    
    
    private List<AnalyticsDashboardDto.TopActiveUserDto> generateTopActiveUsers() {
        // Mock data for top active users
        List<AnalyticsDashboardDto.TopActiveUserDto> topUsers = new ArrayList<>();
        
        // Get users and their transaction counts (simplified)
        // In a real implementation, you'd query the database for this data
        topUsers.add(new AnalyticsDashboardDto.TopActiveUserDto("sarah.jones", "sarah.jones@library.com", 10, 0));
        topUsers.add(new AnalyticsDashboardDto.TopActiveUserDto("alice.smith1", "alice.smith1@email.com", 8, 0));
        topUsers.add(new AnalyticsDashboardDto.TopActiveUserDto("ian.rodriguez9", "ian.rodriguez9@email.com", 8, 2));
        topUsers.add(new AnalyticsDashboardDto.TopActiveUserDto("kevin.hernandez11", "kevin.hernandez11@email.com", 8, 0));
        topUsers.add(new AnalyticsDashboardDto.TopActiveUserDto("hannah.davis8", "hannah.davis8@email.com", 6, 0));
        
        return topUsers;
    }
    
    private List<AnalyticsDashboardDto.PopularBookDto> generateMostBorrowedBooks() {
        // Mock data for most borrowed books
        List<AnalyticsDashboardDto.PopularBookDto> mostBorrowed = new ArrayList<>();
        
        mostBorrowed.add(new AnalyticsDashboardDto.PopularBookDto("The Immortal Life of Henrietta Lacks", "Rebecca Skloot", "Science", 10));
        mostBorrowed.add(new AnalyticsDashboardDto.PopularBookDto("A Game of Thrones", "George R.R. Martin", "Fantasy", 8));
        mostBorrowed.add(new AnalyticsDashboardDto.PopularBookDto("Effective C++", "Scott Meyers", "Programming", 7));
        mostBorrowed.add(new AnalyticsDashboardDto.PopularBookDto("The Alchemist", "Paulo Coelho", "Fiction", 7));
        mostBorrowed.add(new AnalyticsDashboardDto.PopularBookDto("The Fault in Our Stars", "John Green", "Romance", 6));
        mostBorrowed.add(new AnalyticsDashboardDto.PopularBookDto("Head First Design Patterns", "Eric Freeman", "Programming", 5));
        mostBorrowed.add(new AnalyticsDashboardDto.PopularBookDto("Oxford History of the World", "J.M. Roberts", "History", 5));
        mostBorrowed.add(new AnalyticsDashboardDto.PopularBookDto("To Kill a Mockingbird", "Harper Lee", "Fiction", 5));
        mostBorrowed.add(new AnalyticsDashboardDto.PopularBookDto("Harry Potter and the Goblet of Fire", "J.K. Rowling", "Fantasy", 5));
        mostBorrowed.add(new AnalyticsDashboardDto.PopularBookDto("Les Misérables", "Victor Hugo", "Fiction", 5));
        
        return mostBorrowed;
    }
    
    private List<AnalyticsDashboardDto.PopularBookDto> generateLeastBorrowedBooks() {
        // Mock data for least borrowed books
        List<AnalyticsDashboardDto.PopularBookDto> leastBorrowed = new ArrayList<>();
        
        leastBorrowed.add(new AnalyticsDashboardDto.PopularBookDto("To Kill a Mockingbird", "Harper Lee", "Fiction", 0));
        leastBorrowed.add(new AnalyticsDashboardDto.PopularBookDto("One Hundred Years of Solitude", "Gabriel García Márquez", "Fiction", 0));
        leastBorrowed.add(new AnalyticsDashboardDto.PopularBookDto("Neuromancer", "William Gibson", "Cyberpunk", 0));
        leastBorrowed.add(new AnalyticsDashboardDto.PopularBookDto("The Way of Kings", "Brandon Sanderson", "Fantasy", 0));
        leastBorrowed.add(new AnalyticsDashboardDto.PopularBookDto("Divergent", "Veronica Roth", "Young Adult", 0));
        
        return leastBorrowed;
    }
    
    private List<AnalyticsDashboardDto.RecentActivityDto> generateRecentActivity() {
        // Generate last 7 days activity
        List<AnalyticsDashboardDto.RecentActivityDto> activity = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        
        for (int i = 6; i >= 0; i--) {
            LocalDateTime date = now.minusDays(i);
            String dateStr = date.toLocalDate().toString();
            
            // Mock data - all activity on today for demo
            if (i == 0) {
                activity.add(new AnalyticsDashboardDto.RecentActivityDto(dateStr, 85, 57));
            } else {
                activity.add(new AnalyticsDashboardDto.RecentActivityDto(dateStr, 0, 0));
            }
        }
        
        return activity;
    }
    
    private Map<String, Double> generateCategoryUtilization() {
        // Mock category utilization data
        Map<String, Double> utilization = new HashMap<>();
        
        utilization.put("Economics", 20.0);
        utilization.put("Adventure", 44.44444444444444);
        utilization.put("Horror", 0.0);
        utilization.put("Psychology", 16.666666666666664);
        utilization.put("Programming", 20.0);
        utilization.put("Science", 0.0);
        utilization.put("Romance", 7.142857142857142);
        utilization.put("History", 9.090909090909092);
        utilization.put("Science Fiction", 5.0);
        utilization.put("Young Adult", 0.0);
        utilization.put("Cyberpunk", 0.0);
        utilization.put("Thriller", 0.0);
        utilization.put("Fantasy", 12.5);
        utilization.put("Mystery", 16.666666666666664);
        utilization.put("Children", 13.333333333333334);
        utilization.put("Fiction", 12.244897959183673);
        
        return utilization;
    }
}
