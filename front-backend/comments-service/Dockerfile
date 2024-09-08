# Étape de build (compilation) avec Maven
FROM openjdk:17-jdk-slim
WORKDIR /app

# Copier les artefacts de build depuis l'étape de compilation
COPY target/comments-service-0.0.1-SNAPSHOT.jar comments.jar

# Exposer le port nécessaire pour votre application
EXPOSE 8086

# Commande de démarrage de l'application
CMD ["java", "-jar", "comments.jar"]
