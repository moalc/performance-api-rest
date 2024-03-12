FROM maven:3.9.0-eclipse-temurin-17-alpine AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:19
COPY --from= build /target/performance-api-rest-0.0.1-SNAPSHOT.jar performance-api-rest-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "performance-api-rest-0.0.1-SNAPSHOT.jar"]