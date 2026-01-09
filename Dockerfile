# Use OpenJDK 21 runtime image
FROM eclipse-temurin:21-jre

# Set workdir
WORKDIR /app

# Copy built jar from target folder
COPY target/*.jar app.jar

# Expose the port used in your application (default 8080)
EXPOSE 9090

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
