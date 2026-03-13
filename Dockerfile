# Stage 1: Build the application
# Use Maven with Java 17 to compile and package the app
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copy pom.xml first — Docker caches this layer so dependencies
# aren't re-downloaded every time we change source code
COPY pom.xml .
RUN mvn dependency:go-offline

# Now copy source code and build the jar
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run the application
# Use a smaller JRE image (not the full JDK) for the final container
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy only the built jar from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the port the app runs on
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
