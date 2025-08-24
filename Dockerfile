# Simple runtime image for Spring Boot Modulith Application
FROM eclipse-temurin:22-jre-alpine

# Set working directory
WORKDIR /app

# Copy JAR from target folder
COPY target/library-modulith-1.0.0.jar app.jar

# Expose port
EXPOSE 8080

# Environment variables with defaults
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
