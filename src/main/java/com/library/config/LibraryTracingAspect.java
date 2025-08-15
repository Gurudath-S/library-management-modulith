package com.library.config;

import io.micrometer.observation.annotation.Observed;
import io.micrometer.tracing.Tracer;
import io.micrometer.tracing.Span;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Aspect for adding distributed tracing to library operations
 */
@Aspect
@Component
public class LibraryTracingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LibraryTracingAspect.class);

    @Autowired
    private Tracer tracer;

    @Around("execution(* com.library.transactions.TransactionService.borrowBook(..))")
    public Object traceBorrowBook(ProceedingJoinPoint joinPoint) throws Throwable {
        Span span = tracer.nextSpan()
                .name("library.transaction.borrow")
                .tag("operation", "borrow")
                .tag("service", "transaction-service")
                .start();

        try {
            Object[] args = joinPoint.getArgs();
            if (args.length >= 2) {
                span.tag("user.id", String.valueOf(args[0]));
                span.tag("book.id", String.valueOf(args[1]));
            }
            
            Object result = joinPoint.proceed();
            span.tag("result", "success");
            return result;
        } catch (Exception e) {
            span.tag("error", e.getMessage());
            span.tag("result", "failure");
            throw e;
        } finally {
            span.end();
        }
    }

    @Around("execution(* com.library.transactions.TransactionService.returnBook(..))")
    public Object traceReturnBook(ProceedingJoinPoint joinPoint) throws Throwable {
        Span span = tracer.nextSpan()
                .name("library.transaction.return")
                .tag("operation", "return")
                .tag("service", "transaction-service")
                .start();

        try {
            Object[] args = joinPoint.getArgs();
            if (args.length >= 1) {
                span.tag("transaction.id", String.valueOf(args[0]));
            }
            
            Object result = joinPoint.proceed();
            span.tag("result", "success");
            return result;
        } catch (Exception e) {
            span.tag("error", e.getMessage());
            span.tag("result", "failure");
            throw e;
        } finally {
            span.end();
        }
    }

    @Around("execution(* com.library.users.UserService.createUser(..))")
    public Object traceUserRegistration(ProceedingJoinPoint joinPoint) throws Throwable {
        Span span = tracer.nextSpan()
                .name("library.user.registration")
                .tag("operation", "registration")
                .tag("service", "user-service")
                .start();

        try {
            Object result = joinPoint.proceed();
            span.tag("result", "success");
            return result;
        } catch (Exception e) {
            span.tag("error", e.getMessage());
            span.tag("result", "failure");
            throw e;
        } finally {
            span.end();
        }
    }

    @Around("execution(* com.library.books.BookService.addBook(..))")
    public Object traceBookAddition(ProceedingJoinPoint joinPoint) throws Throwable {
        Span span = tracer.nextSpan()
                .name("library.book.add")
                .tag("operation", "add")
                .tag("service", "book-service")
                .start();

        try {
            Object result = joinPoint.proceed();
            span.tag("result", "success");
            return result;
        } catch (Exception e) {
            span.tag("error", e.getMessage());
            span.tag("result", "failure");
            throw e;
        } finally {
            span.end();
        }
    }

    @Around("execution(* com.library.analytics.AnalyticsService.generateDashboard(..))")
    public Object traceAnalyticsDashboard(ProceedingJoinPoint joinPoint) throws Throwable {
        Span span = tracer.nextSpan()
                .name("library.analytics.dashboard")
                .tag("operation", "dashboard")
                .tag("service", "analytics-service")
                .start();

        try {
            long startTime = System.currentTimeMillis();
            Object result = joinPoint.proceed();
            long duration = System.currentTimeMillis() - startTime;
            
            span.tag("result", "success");
            span.tag("duration.ms", String.valueOf(duration));
            return result;
        } catch (Exception e) {
            span.tag("error", e.getMessage());
            span.tag("result", "failure");
            throw e;
        } finally {
            span.end();
        }
    }
}
