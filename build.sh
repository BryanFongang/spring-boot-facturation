# build.sh
#!/bin/bash
echo "🚀 Construction de l'API CamerExpress Pricing..."

# Nettoyage
mvn clean



# Construction du JAR
echo "📦 Construction du JAR..."
mvn package -DskipTests

# Construction de l'image Docker
echo "🐳 Construction de l'image Docker..."
docker build -t camerexpress/pricing-api:2.1.0 .
docker tag camerexpress/pricing-api:2.1.0 camerexpress/pricing-api:latest

echo "✅ Construction terminée!