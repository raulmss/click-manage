# Start from OpenJDK 17 base image
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy and build the project
COPY target/spring-security-refresh-token-0.0.1-SNAPSHOT.jar app.jar

# Run the jar file
ENTRYPOINT ["java","-jar","/app/app.jar"]
