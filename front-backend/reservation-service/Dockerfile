# Étape de compilation avec Maven
FROM maven AS builder
WORKDIR app
COPY . .
RUN mvn clean install -DskipTests
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY  --from=builder /app/target/reservation-service-0.0.1-SNAPSHOT.jar reservation.jar
EXPOSE 8087
CMD ["java", "-jar", "reservation.jar"]