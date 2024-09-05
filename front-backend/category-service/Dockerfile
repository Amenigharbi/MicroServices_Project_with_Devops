# Étape de build (compilation) avec Maven
FROM maven AS build
WORKDIR /app

# Copier les fichiers du projet (y compris pom.xml et le code source)
COPY . .

# Compiler et installer le projet avec Maven, en ignorant les tests
RUN mvn clean install

# Étape de construction de l'image finale avec OpenJDK
FROM openjdk:17-jdk-slim
WORKDIR /app

# Copier les artefacts de build depuis l'étape de compilation
COPY --from=build /app/target/category-service-0.0.1-SNAPSHOT.jar category-service.jar

# Exposer le port nécessaire pour votre application
EXPOSE 8085

# Commande de démarrage de l'application
CMD ["java", "-jar", "category-service.jar"]
