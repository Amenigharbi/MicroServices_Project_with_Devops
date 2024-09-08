

# Étape de construction de l'image finale avec OpenJDK
FROM openjdk:17-jdk-slim
WORKDIR /app

# Copier les artefacts de build depuis l'étape de compilation
COPY target/reservation-service-0.0.1-SNAPSHOT.jar reservation.jar

# Exposer le port nécessaire pour votre application
EXPOSE 8087

# Commande de démarrage de l'application
CMD ["java", "-jar", "reservation.jar"]
