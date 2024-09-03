# Étape de construction de l'image finale
FROM openjdk:17-jdk-slim

# Définir le répertoire de travail
WORKDIR /app

COPY target/comments-service-0.0.1-SNAPSHOT.jar comment.jar

# Exposer le port nécessaire pour votre application
EXPOSE 8086

# Commande de démarrage de l'application
CMD ["java", "-jar", "comment.jar"]
