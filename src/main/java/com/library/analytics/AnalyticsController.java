package com.library.analytics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/analytics")
@CrossOrigin(origins = "*")
public class AnalyticsController {
    
    @Autowired
    private AnalyticsModuleAPI analyticsModuleAPI;
    
    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ADMIN') or hasRole('LIBRARIAN')")
    public ResponseEntity<?> getDashboard() {
        try {
            long startTime = System.currentTimeMillis();
            
            AnalyticsDashboardDto dashboard = analyticsModuleAPI.generateDashboard();
            
            long executionTime = System.currentTimeMillis() - startTime;
            
            // Add execution metadata for performance analysis
            Map<String, Object> response = new HashMap<>();
            response.put("dashboard", dashboard);
            response.put("metadata", Map.of(
                "executionTimeMs", executionTime,
                "generatedAt", dashboard.getGeneratedAt(),
                "dataFreshness", "REAL_TIME"
            ));
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of(
                    "error", "Failed to generate analytics dashboard",
                    "message", e.getMessage(),
                    "timestamp", System.currentTimeMillis()
                ));
        }
    }
    
    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN') or hasRole('LIBRARIAN')")
    public ResponseEntity<?> getUserAnalytics() {
        try {
            AnalyticsDashboardDto.UserAnalyticsDto userAnalytics = analyticsModuleAPI.generateUserAnalytics();
            
            return ResponseEntity.ok(Map.of(
                "userAnalytics", userAnalytics,
                "generatedAt", java.time.LocalDateTime.now()
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Failed to generate user analytics"));
        }
    }
    
    @GetMapping("/books")
    @PreAuthorize("hasRole('ADMIN') or hasRole('LIBRARIAN')")
    public ResponseEntity<?> getBookAnalytics() {
        try {
            AnalyticsDashboardDto.BookAnalyticsDto bookAnalytics = analyticsModuleAPI.generateBookAnalytics();
            
            return ResponseEntity.ok(Map.of(
                "bookAnalytics", bookAnalytics,
                "generatedAt", java.time.LocalDateTime.now()
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Failed to generate book analytics"));
        }
    }
    
    @GetMapping("/transactions")
    @PreAuthorize("hasRole('ADMIN') or hasRole('LIBRARIAN')")
    public ResponseEntity<?> getTransactionAnalytics() {
        try {
            AnalyticsDashboardDto.TransactionAnalyticsDto transactionAnalytics = analyticsModuleAPI.generateTransactionAnalytics();
            
            return ResponseEntity.ok(Map.of(
                "transactionAnalytics", transactionAnalytics,
                "generatedAt", java.time.LocalDateTime.now()
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Failed to generate transaction analytics"));
        }
    }
    
    @GetMapping("/inventory")
    @PreAuthorize("hasRole('ADMIN') or hasRole('LIBRARIAN')")
    public ResponseEntity<?> getInventoryAnalytics() {
        try {
            AnalyticsDashboardDto.InventoryAnalyticsDto inventoryAnalytics = analyticsModuleAPI.generateInventoryAnalytics();
            
            return ResponseEntity.ok(Map.of(
                "inventoryAnalytics", inventoryAnalytics,
                "generatedAt", java.time.LocalDateTime.now()
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Failed to generate inventory analytics"));
        }
    }
    
    @GetMapping("/health")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getSystemHealth() {
        try {
            AnalyticsDashboardDto.SystemHealthDto systemHealth = analyticsModuleAPI.generateSystemHealth();
            
            return ResponseEntity.ok(Map.of(
                "systemHealth", systemHealth,
                "generatedAt", java.time.LocalDateTime.now()
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Failed to generate system health analytics"));
        }
    }
}
