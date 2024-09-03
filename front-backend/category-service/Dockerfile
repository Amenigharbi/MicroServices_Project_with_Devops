FROM openjdk:17-jdk-slim

# Définir le répertoire de travail
WORKDIR /app

COPY target/category-service-0.0.1-SNAPSHOT.jar category.jar

# Exposer le port nécessaire pour votre application
EXPOSE 8085

# Commande de démarrage de l'application
CMD ["java", "-jar", "category.jar"]
