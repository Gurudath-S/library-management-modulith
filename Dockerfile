# Simple runtime image for Spring Boot Modulith Application
FROM eclipse-temurin:22-jre-jammy

# Install curl for health checks
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Create non-root user for security
RUN groupadd -r spring && useradd -r -g spring spring

# Set working directory
WORKDIR /app

# Copy JAR from target folder
COPY target/library-modulith-1.0.0.jar app.jar

# Change ownership of the application
RUN chown -R spring:spring /app

# Switch to non-root user
USER spring

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Environment variables with defaults
ENV SPRING_PROFILES_ACTIVE=default
ENV JAVA_OPTS="-Xmx512m -Xms256m"
ENV SERVER_PORT=8080

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
