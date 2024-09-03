# Étape de construction de l'image finale
FROM openjdk:17-jdk-slim

# Définir le répertoire de travail
WORKDIR /app

# Copier les artefacts de build depuis l'étape de compilation
COPY target/springsecurity-service-0.0.1-SNAPSHOT.jar springsecurity.jar

# Exposer le port nécessaire pour votre application
EXPOSE 8084

# Commande de démarrage de l'application
CMD ["java", "-jar", "springsecurity.jar"]
