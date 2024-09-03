FROM openjdk:17-jdk-slim
WORKDIR app
COPY target/api-gateway-service-0.0.1-SNAPSHOT.jar api-gateway.jar
EXPOSE 8089
CMD ["java", "-jar", "api-gateway.jar"]
