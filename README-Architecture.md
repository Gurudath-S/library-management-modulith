# Library Management Modulith - Architecture Documentation

## System Overview

**Architecture Pattern**: Spring Modulith
**Framework**: Spring Boot 3.3.0
**System Name**: Library Management System
**Primary Pattern**: Modular Monolith with Event-Driven Communication

## Core Architectural Components

### Application Entry Point
- **LibraryModulithApplication**: Main application class with `@Modulith` annotation
- **Configuration**: Defines shared modules and system boundaries
- **Shared Module Declaration**: Explicitly declares "shared" as common dependency

### Module Structure

#### 1. User Management Module (`users`)
**Architectural Purpose**: Authentication, authorization, and user lifecycle management
**Dependencies**: `shared` module only
**Responsibilities**:
- User authentication and session management
- Role-based access control (RBAC)
- JWT token generation and validation
- User profile management

**Components**:
- **AuthController**: Authentication endpoint handler
- **UserController**: User management REST interface
- **UserService**: Business logic layer implementing UserModuleAPI
- **CustomUserDetailsService**: Spring Security integration
- **SecurityConfig**: Security configuration and filter chains
- **JwtAuthenticationFilter**: JWT token processing filter
- **JwtUtils**: Token generation and validation utilities
- **UserRepository**: Data access layer
- **UserModuleAPI**: Module boundary interface for external access

#### 2. Book Management Module (`books`)
**Architectural Purpose**: Book catalog and inventory management
**Dependencies**: `shared` module only
**Responsibilities**:
- Book catalog operations
- Inventory tracking and availability management
- Category-based organization
- Search and filtering capabilities

**Components**:
- **BookController**: Book management REST interface
- **BookService**: Business logic layer implementing BookModuleAPI
- **BookRepository**: Data access layer with custom queries
- **BookModuleAPI**: Module boundary interface for external access
- **Event Publishing**: BookAddedEvent integration

#### 3. Transaction Management Module (`transactions`)
**Architectural Purpose**: Book borrowing and return transaction processing
**Dependencies**: `shared`, `users`, `books` modules
**Responsibilities**:
- Transaction lifecycle management
- Due date calculation and tracking
- Overdue transaction monitoring
- Cross-module coordination for borrowing/returning

**Components**:
- **TransactionController**: Transaction management REST interface
- **TransactionService**: Business logic layer implementing TransactionModuleAPI
- **TransactionRepository**: Data access layer with temporal queries
- **TransactionModuleAPI**: Module boundary interface for external access
- **Event Publishing**: BookBorrowedEvent, BookReturnedEvent integration

#### 4. Analytics Module (`analytics`)
**Architectural Purpose**: Cross-module data aggregation and reporting
**Dependencies**: `shared`, `users`, `books`, `transactions` modules
**Responsibilities**:
- Dashboard data aggregation
- Performance metrics calculation
- System health monitoring
- Cross-module analytics synthesis

**Components**:
- **AnalyticsController**: Analytics REST interface
- **AnalyticsService**: Aggregation logic implementing AnalyticsModuleAPI
- **AnalyticsModuleAPI**: Module boundary interface
- **AnalyticsDashboardDto**: Composite data transfer objects

#### 5. Shared Module (`shared`)
**Architectural Purpose**: Common contracts and cross-cutting concerns
**Dependencies**: None (foundation module)
**Responsibilities**:
- Event definitions and contracts
- Common DTOs and value objects
- Cross-module communication protocols

**Components**:
- **Event Definitions**: BookAddedEvent, BookBorrowedEvent, BookReturnedEvent, UserRegisteredEvent
- **Common Contracts**: Shared interfaces and data structures

## Inter-Module Communication Patterns

### Module API Pattern
- **Purpose**: Type-safe module boundaries
- **Implementation**: Interface-based contracts (UserModuleAPI, BookModuleAPI, etc.)
- **Dependency Injection**: Spring-managed inter-module dependencies
- **Benefits**: Loose coupling, clear contracts, testability

### Event-Driven Communication
- **Framework**: Spring Modulith Events
- **Event Types**: Domain events with `@Externalized` annotation
- **Async Processing**: Application event publisher pattern
- **Event Store**: Persistent event tracking capability

### Dependency Flow
```
Analytics ← Transactions ← Books
    ↑           ↑          ↑
    └─── Users ←┘          │
         ↑                 │
         └─── Shared ←─────┘
```

## Security Architecture

### Authentication Layer
- **JWT-based Stateless Authentication**: Token-based session management
- **BCrypt Password Encoding**: Secure password storage
- **Custom UserDetailsService**: Spring Security integration

### Authorization Layer
- **Method-level Security**: `@PreAuthorize` annotations
- **Role-based Access Control**: ADMIN, LIBRARIAN, USER roles
- **Security Filter Chain**: Custom JWT authentication filter

### Cross-Cutting Security
- **CORS Configuration**: Cross-origin resource sharing setup
- **Session Management**: Stateless session policy
- **Authentication Provider**: DAO-based authentication

## Data Flow Patterns

### Request Processing Flow
1. **Controller Layer**: REST endpoint handling
2. **Service Layer**: Business logic processing
3. **Repository Layer**: Data persistence operations
4. **Event Publishing**: Asynchronous event notification

### Cross-Module Data Access
- **Module APIs**: Controlled access through interfaces
- **Event-driven Updates**: Reactive data synchronization
- **Transactional Boundaries**: Module-level transaction management

## Design Patterns Implementation

### Module Pattern
- **Encapsulation**: Package-private components with public APIs
- **Boundary Enforcement**: `@ApplicationModule` annotations
- **Dependency Control**: Explicit allowed dependencies

### Repository Pattern
- **Data Access Abstraction**: JPA repository interfaces
- **Custom Query Methods**: Named queries and JPQL
- **Transaction Management**: `@Transactional` boundaries

### Event Sourcing Pattern
- **Domain Events**: Business event publication
- **Event Externalization**: `@Externalized` for external systems
- **Event-driven Architecture**: Loose coupling through events

### DTO Pattern
- **Data Transfer Objects**: Clean API contracts
- **Module Boundary DTOs**: Type-safe inter-module communication
- **Composite DTOs**: Aggregated data structures for analytics

## Technology Stack Architecture

### Core Framework
- **Spring Boot 3.3.0**: Application foundation
- **Spring Modulith**: Modular architecture support
- **Spring Security**: Authentication and authorization
- **Spring Data JPA**: Data access layer

### Communication Infrastructure
- **Spring Web**: REST API framework
- **Spring Events**: Inter-module communication
- **Jackson**: JSON serialization/deserialization

### Persistence Layer
- **JPA/Hibernate**: Object-relational mapping
- **Database Agnostic**: Repository abstraction
- **Transaction Management**: Declarative transactions

## Integration Points

### External System Integration
- **REST API**: External client integration
- **Event Externalization**: External event processing
- **Security Token**: JWT for external authentication

### Internal Integration
- **Module APIs**: Type-safe internal communication
- **Event Bus**: Internal event distribution
- **Shared Contracts**: Common data structures and interfaces
