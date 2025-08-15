package com.library.config;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Configuration for custom Prometheus metrics
 */
@Configuration
public class MetricsConfiguration {

    private final AtomicInteger activeUsers = new AtomicInteger(0);
    private final AtomicInteger activeBorrows = new AtomicInteger(0);

    @Bean
    public MeterRegistryCustomizer<MeterRegistry> metricsCommonTags() {
        return registry -> registry.config().commonTags("application", "library-modulith");
    }

    /**
     * Custom metrics for library operations
     */
    @Bean
    public Timer bookBorrowTimer(MeterRegistry meterRegistry) {
        return Timer.builder("library.book.borrow.duration")
                .description("Time taken to borrow a book")
                .tag("operation", "borrow")
                .register(meterRegistry);
    }

    @Bean
    public Timer bookReturnTimer(MeterRegistry meterRegistry) {
        return Timer.builder("library.book.return.duration")
                .description("Time taken to return a book")
                .tag("operation", "return")
                .register(meterRegistry);
    }

    @Bean
    public Counter bookBorrowCounter(MeterRegistry meterRegistry) {
        return Counter.builder("library.book.operations.total")
                .description("Total number of book operations")
                .tag("operation", "borrow")
                .register(meterRegistry);
    }

    @Bean
    public Counter bookReturnCounter(MeterRegistry meterRegistry) {
        return Counter.builder("library.book.operations.total")
                .description("Total number of book operations")
                .tag("operation", "return")
                .register(meterRegistry);
    }

    @Bean
    public Counter userRegistrationCounter(MeterRegistry meterRegistry) {
        return Counter.builder("library.user.registrations.total")
                .description("Total number of user registrations")
                .register(meterRegistry);
    }

    @Bean
    public Counter loginAttemptsCounter(MeterRegistry meterRegistry) {
        return Counter.builder("library.auth.login.attempts.total")
                .description("Total number of login attempts")
                .register(meterRegistry);
    }

    @Bean
    public Counter loginSuccessCounter(MeterRegistry meterRegistry) {
        return Counter.builder("library.auth.login.success.total")
                .description("Total number of successful logins")
                .register(meterRegistry);
    }

    @Bean
    public Counter loginFailureCounter(MeterRegistry meterRegistry) {
        return Counter.builder("library.auth.login.failures.total")
                .description("Total number of failed logins")
                .register(meterRegistry);
    }

    /**
     * Gauge for active users
     */
    @Bean
    public Gauge activeUsersGauge(MeterRegistry meterRegistry) {
        return Gauge.builder("library.users.active", activeUsers, obj -> obj.get())
                .description("Number of currently active users")
                .register(meterRegistry);
    }

    /**
     * Gauge for active borrows
     */
    @Bean
    public Gauge activeBorrowsGauge(MeterRegistry meterRegistry) {
        return Gauge.builder("library.borrows.active", activeBorrows, obj -> obj.get())
                .description("Number of currently active borrows")
                .register(meterRegistry);
    }

    // Getters for updating metrics
    public AtomicInteger getActiveUsers() {
        return activeUsers;
    }

    public AtomicInteger getActiveBorrows() {
        return activeBorrows;
    }
}
