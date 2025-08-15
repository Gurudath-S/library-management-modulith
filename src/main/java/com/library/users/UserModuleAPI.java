package com.library.users;

/**
 * Module API for User operations
 * This interface defines the public API that other modules can use
 * to interact with the users module
 */
public interface UserModuleAPI {
    
    /**
     * Analytics data structures
     */
    record UserAnalytics(
        long totalUsers,
        long activeUsers,
        long newUsersThisMonth,
        long adminCount,
        long librarianCount,
        long userCount
    ) {}
    
    record UserInfo(
        Long id,
        String username, 
        String email,
        String fullName,
        String role
    ) {}
    
    /**
     * Get comprehensive user analytics
     */
    UserAnalytics getUserAnalytics();
    
    /**
     * Get user information by ID
     */
    UserInfo getUserInfo(Long userId);
    
    /**
     * Get user information by username
     */
    UserInfo getUserInfoByUsername(String username);
    
    /**
     * Check if user exists
     */
    boolean userExists(Long userId);
    
    /**
     * Get total user count
     */
    long getTotalUsers();
    
    /**
     * Get active user count  
     */
    long getActiveUsers();
    
    /**
     * Get new users this month
     */
    long getNewUsersThisMonth();
    
    /**
     * Get total users count
     */
    long getTotalUsersCount();
    
    /**
     * Get user count by role
     */
    long getUserCountByRole(String role);
    
    /**
     * Get users created in a specific month
     */
    long getUsersCreatedInMonth(int year, int month);
}
