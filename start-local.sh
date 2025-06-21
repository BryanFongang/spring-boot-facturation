# start-local.sh
#!/bin/bash
echo "🚀 Démarrage local de l'API CamerExpress Pricing..."

# Vérifier si Maven est installé
if ! command -v mvn &> /dev/null
then
    echo "❌ Maven n'est pas installé. Veuillez installer Maven d'abord."
    exit 1
fi

# Vérifier si Java 17 est installé
if ! java -version 2>&1 | grep -q "17"
then
    echo "⚠️ Java 17 n'est pas installé ou n'est pas la version par défaut."
    echo "Tentative de démarrage quand même..."
fi

# Construction du projet
echo "📦 Construction du projet..."
mvn clean package -DskipTests

# Vérifier si le JAR a été créé
if [ ! -f "target/pricing-api-2.1.0.jar" ]; then
    echo "❌ Erreur lors de la construction du JAR"
    exit 1
fi

# Démarrage de l'application
echo "▶️ Démarrage de l'application..."
echo "🌐 L'API sera disponible sur http://localhost:8080"
echo "📚 Documentation Swagger sur http://localhost:8080/swagger-ui.html"
echo ""
echo "Pour arrêter l'application, appuyez sur Ctrl+C"
echo ""

java -jar target/pricing-api-2.1.0.jar