# Étape de construction
FROM maven AS build
WORKDIR app
COPY . .
RUN mvn install -DskipTests

# Étape d'exécution
FROM openjdk:17
WORKDIR /app
COPY --from=build /app/target/api-gateway-service-0.0.1-SNAPSHOT.jar api-gateway.jar
EXPOSE 8762
CMD ["java", "-jar", "api-gateway.jar"]
