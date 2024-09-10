FROM openjdk:17-jdk-slim
WORKDIR /app

# Copier les artefacts de build depuis l'étape de compilation
COPY target/discovery-service-0.0.1-SNAPSHOT.jar discovery.jar

# Exposer le port nécessaire pour votre application
EXPOSE 8761

# Commande de démarrage de l'application
CMD ["java", "-jar", "discovery.jar"]
