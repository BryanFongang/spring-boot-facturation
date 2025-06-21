# ================= STAGE 1: Build =================
# Utilise une image Docker qui contient Maven et Java 17
FROM maven:3.8.5-openjdk-17 AS build

# Définit le répertoire de travail
WORKDIR /app

# Copie les fichiers de configuration de Maven
COPY pom.xml .

# Copie tout le code source
COPY src ./src

# Exécute la commande Maven pour construire le projet et créer le .jar
# -DskipTests pour ne pas lancer les tests pendant le build
RUN mvn clean package -DskipTests


# ================= STAGE 2: Final Image =================
# Part d'une image légère contenant uniquement Java 17, pas Maven
FROM openjdk:17-jdk-slim

# Maintient vos labels d'information
LABEL maintainer="backend@camerexpress.com"
LABEL description="CamerExpress Pricing API"
LABEL version="2.1.0"

# Définit le répertoire de travail
WORKDIR /app

# Copie UNIQUEMENT le .jar qui a été créé dans le stage "build"
COPY --from=build /app/target/pricing-api-2.1.0.jar app.jar

# Expose le port 8080
EXPOSE 8080

# Configure les variables d'environnement
ENV SPRING_PROFILES_ACTIVE=prod
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Définit la commande de démarrage
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]