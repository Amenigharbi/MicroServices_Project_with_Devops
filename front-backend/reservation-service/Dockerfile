# Étape de compilation avec Maven
FROM openjdk:17-jdk-slim
WORKDIR app
COPY target/reservation-service-0.0.1-SNAPSHOT.jar reservation.jar
EXPOSE 8087
CMD ["java", "-jar", "reservation.jar"]