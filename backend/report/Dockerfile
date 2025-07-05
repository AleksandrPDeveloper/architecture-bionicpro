# Use an official JDK image as the base image
FROM eclipse-temurin:21-alpine AS builder

# Set the working directory
WORKDIR /app

# Copy the build.gradle and settings.gradle files
COPY build.gradle settings.gradle ./

# Copy the Gradle wrapper files
COPY gradlew ./
COPY gradle ./gradle

# Copy the source code
COPY src ./src

# Build the application
RUN ./gradlew build --no-daemon

# Use a smaller JDK image for running the application
FROM eclipse-temurin:21-alpine

# Set the working directory
WORKDIR /app

# Copy the built JAR file from the builder stage
COPY --from=builder /app/build/libs/*.jar ./

# Expose the application port
EXPOSE 8000

# Command to run the application
ENTRYPOINT ["java", "-jar", "report-0.0.1-SNAPSHOT.jar"]
