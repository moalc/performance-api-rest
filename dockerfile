FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17.0.1-jdk-slim
COPY --from=build /target/performance-api-rest-0.0.1-SNAPSHOT.jar performance-api-rest.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "performance-api-rest.jar"]