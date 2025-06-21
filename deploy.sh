# deploy.sh
#!/bin/bash
echo "🚀 Déploiement de l'API CamerExpress Pricing..."

# Arrêt des conteneurs existants
echo "🛑 Arrêt des conteneurs existants..."
docker-compose down

# Construction
echo "🔨 Reconstruction des images..."
docker-compose build --no-cache

# Démarrage
echo "▶️ Démarrage des services..."
docker-compose up -d

# Vérification
echo "🔍 Vérification de l'état des services..."
sleep 10
docker-compose ps

echo "🌐 L'API est disponible sur :"
echo "   - Local: http://localhost:8080"
echo "   - Swagger UI: http://localhost:8080/swagger-ui.html"
echo "   - Health: http://localhost:8080/api/v1/pricing/health"

echo "✅ Déploiement terminé!"