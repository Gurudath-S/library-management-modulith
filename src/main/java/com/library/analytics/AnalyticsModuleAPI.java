package com.library.analytics;

public interface AnalyticsModuleAPI {
    
    /**
     * Generate complete analytics dashboard
     */
    AnalyticsDashboardDto generateDashboard();
    
    /**
     * Generate user analytics
     */
    AnalyticsDashboardDto.UserAnalyticsDto generateUserAnalytics();
    
    /**
     * Generate book analytics
     */
    AnalyticsDashboardDto.BookAnalyticsDto generateBookAnalytics();
    
    /**
     * Generate transaction analytics
     */
    AnalyticsDashboardDto.TransactionAnalyticsDto generateTransactionAnalytics();
    
    /**
     * Generate inventory analytics
     */
    AnalyticsDashboardDto.InventoryAnalyticsDto generateInventoryAnalytics();
    
    /**
     * Generate system health metrics
     */
    AnalyticsDashboardDto.SystemHealthDto generateSystemHealth();
}
