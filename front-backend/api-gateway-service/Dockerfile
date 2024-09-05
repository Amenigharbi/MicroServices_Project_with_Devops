# Étape de compilation avec Maven
FROM maven AS build
WORKDIR app
COPY . .
RUN mvn clean
RUN mvn clean install
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY  --from=builder /app/target/api-gateway-service-0.0.1-SNAPSHOT.jar api-gateway.jar
EXPOSE 8762
CMD ["java", "-jar", "api-gateway.jar"]