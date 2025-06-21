FROM openjdk:17-jdk-slim

LABEL maintainer="backend@camerexpress.com"
LABEL description="CamerExpress Pricing API"
LABEL version="2.1.0"

WORKDIR /app

# Copier le JAR de l'application
COPY target/pricing-api-2.1.0.jar app.jar

# Exposer le port 8080
EXPOSE 8080

# Variables d'environnement
ENV SPRING_PROFILES_ACTIVE=prod
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Commande de démarrage
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]