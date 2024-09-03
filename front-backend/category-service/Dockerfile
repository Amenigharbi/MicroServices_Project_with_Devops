# Étape de compilation avec Maven
FROM maven AS builder

# Définir le répertoire de travail
WORKDIR app

# Copier les fichiers de configuration Maven (pom.xml) pour télécharger les dépendances
COPY . .

# Copier le reste des sources
COPY . .

# Exécuter le build de l'application sans tests
RUN mvn clean install -DskipTests

# Étape de construction de l'image finale
FROM openjdk:17-jdk-slim

# Définir le répertoire de travail
WORKDIR /app

# Copier les artefacts de build depuis l'étape de compilation
#COPY  ./target/discovery-service-0.0.1-SNAPSHOT.jar /discovery.jar
COPY --from=builder /app/target/category-service-0.0.1-SNAPSHOT.jar category.jar

# Exposer le port nécessaire pour votre application
EXPOSE 8085

# Commande de démarrage de l'application
CMD ["java", "-jar", "category.jar"]
