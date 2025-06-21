package com.camerexpress.pricing.service;

import com.camerexpress.pricing.model.PricingRequest;
import com.camerexpress.pricing.model.PricingResponse;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class PricingCalculationService {

    // Constantes selon le PDF
    private static final double BASE_COST = 500.0; // C0
    private static final double HANDLING_COST = 200.0; // fhandling
    private static final double FUEL_FACTOR = 0.15; // facteur carburant
    private static final double MINIMUM_COST = 300.0; // Contrainte minimum
    private static final double MAXIMUM_COST = 50000.0; // Contrainte maximum

    // Matrice des zones tarifaires (equation 17)
    private static final double[][] ZONE_MATRIX = {
            {500, 1000, 2000},
            {1000, 800, 1500},
            {2000, 1500, 1000}
    };

    public PricingResponse calculatePricing(PricingRequest request) {
        long startTime = System.currentTimeMillis();

        // 1. Calcul du coût de base (Section 3)
        double basePrice = calculateBasePrice(request.getPackageInfo());

        // 2. Calcul du coût géographique (Section 4)
        double geographicCost = calculateGeographicCost(request.getRoute());

        // 3. Calcul du multiplicateur temporel (Section 5)
        double temporalMultiplier = calculateTemporalMultiplier(request.getService());

        // 4. Calcul du coût point relais (Section 6)
        double relayPointCost = calculateRelayPointCost(request.getService());

        // 5. Calcul du coût d'assurance (Section 7)
        double insuranceCost = calculateInsuranceCost(
                request.getPackageInfo().getDeclaredValue(),
                request.getService().getInsurance()
        );

        // 6. Calcul de l'ajustement dynamique (Section 8)
        double dynamicAdjustment = calculateDynamicAdjustment();

        // 7. Calcul de la remise fidélité (Section 9)
        double loyaltyDiscount = calculateLoyaltyDiscount(
                basePrice,
                request.getCustomer() != null ? request.getCustomer().getLoyaltyPoints() : 0
        );

        // Calcul final selon l'équation (1)
        double finalPrice = (basePrice* temporalMultiplier * dynamicAdjustment  )
                + relayPointCost + insuranceCost + geographicCost - loyaltyDiscount;

        // Application des contraintes
        finalPrice = Math.max(MINIMUM_COST, Math.min(MAXIMUM_COST, finalPrice));

        long endTime = System.currentTimeMillis();

        return buildResponse(basePrice, geographicCost, temporalMultiplier,
                relayPointCost, insuranceCost, dynamicAdjustment,
                loyaltyDiscount, finalPrice, (endTime - startTime) + "ms");
    }

    private double calculateBasePrice(PricingRequest.PackageInfo packageInfo) {
        double weight = packageInfo.getWeight();

        // Calcul du poids volumétrique (équation 12)
        double volumetricWeight = (packageInfo.getDimensions().getLength() *
                packageInfo.getDimensions().getWidth() *
                packageInfo.getDimensions().getHeight()) / 5000.0;

        // Poids taxable = max(poids réel, poids volumétrique)
        double taxableWeight = Math.max(weight, volumetricWeight);

        // Fonction de tarification par poids (équation 15)
        double weightRate = getWeightRate(taxableWeight);

        // Coût de base (équation 9)
        double weightCost = taxableWeight * weightRate;
        double fuelCost = weightCost * FUEL_FACTOR;

        return BASE_COST + weightCost + fuelCost + HANDLING_COST;
    }

    private double getWeightRate(double weight) {
        if (weight <= 1) return 1000;
        if (weight <= 5) return 800;
        if (weight <= 10) return 600;
        if (weight <= 25) return 400;
        if (weight <= 50) return 300;
        return 250;
    }

    private double calculateGeographicCost(PricingRequest.RouteInfo route) {
        // Coût inter-zones (équation 18)
        int originZone = route.getOrigin().getZone() - 1; // Index 0-based
        int destZone = route.getDestination().getZone() - 1;
        double zoneCost = ZONE_MATRIX[originZone][destZone];

        // Calcul de la distance réelle avec Haversine (équation 19)
        double distance = calculateHaversineDistance(
                route.getOrigin().getLat(), route.getOrigin().getLng(),
                route.getDestination().getLat(), route.getDestination().getLng()
        );

        // Coût de distance (équation 23)
        double distanceCost = getDistanceCost(distance);

        return zoneCost + distanceCost;
    }

    private double calculateHaversineDistance(double lat1, double lon1, double lat2, double lon2) {
        final double R = 6371; // Rayon terrestre en km

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }

    private double getDistanceCost(double distance) {
        if (distance <= 50) return 10 * distance;
        if (distance <= 200) return 8 * distance;
        if (distance <= 500) return 6 * distance;
        return 4 * distance;
    }

    private double calculateTemporalMultiplier(PricingRequest.ServiceInfo service) {
        // Multiplicateur de vitesse (équation 25)
        double speedMultiplier = getSpeedMultiplier(service.getSpeed());

        // Supplément horaire (équation 26-27)
        double timeSuplement = getTimeSuplement(service.getPickupTime());

        // Facteur de programmation (équation 28)
        double scheduleMultiplier = service.isScheduled() ? 1.2 : 1.0;

        return speedMultiplier * (1 + timeSuplement / BASE_COST) * scheduleMultiplier;
    }

    private double getSpeedMultiplier(String speed) {
        return switch (speed.toUpperCase()) {
            case "STANDARD" -> 1.0;
            case "EXPRESS" -> 1.5;
            case "URGENT" -> 2.0;
            case "INSTANTANE" -> 3.0;
            default -> 1.0;
        };
    }

    private double getTimeSuplement(LocalDateTime pickupTime) {
        int hour = pickupTime.getHour();
        DayOfWeek dayOfWeek = pickupTime.getDayOfWeek();

        // Week-end ou jour férié
        if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
            return 300;
        }

        // Heures de la journée
        if (hour >= 8 && hour < 17) return 0; // Heures ouvrables
        if (hour >= 17 && hour < 20) return 200; // Soirée
        if (hour >= 20 || hour < 6) return 500; // Nuit

        return 0;
    }

    private double calculateRelayPointCost(PricingRequest.ServiceInfo service) {
        String relayType = service.getRelayType();
        if (relayType == null) return 0;

        // Coûts selon le tableau (30)
        return switch (relayType.toUpperCase()) {
            case "PREMIUM" -> 200 + 150; // Cdepot + Cretrait
            case "STANDARD" -> 100 + 100;
            case "BASIC" -> 50 + 50;
            default -> 0;
        };
    }

    private double calculateInsuranceCost(double declaredValue, String insuranceLevel) {
        if (insuranceLevel == null || declaredValue <= 0) return 0;

        // Taux d'assurance par niveau (équation 33)
        double rate = switch (insuranceLevel.toUpperCase()) {
            case "BASIC" -> 0.01;
            case "STANDARD" -> 0.02;
            case "PREMIUM" -> 0.03;
            default -> 0.0;
        };

        // Maximum par niveau
        double maxCoverage = switch (insuranceLevel.toUpperCase()) {
            case "BASIC" -> 50000;
            case "STANDARD" -> 200000;
            case "PREMIUM" -> 1000000;
            default -> 0;
        };

        return Math.min(declaredValue * rate, maxCoverage);
    }

    private double calculateDynamicAdjustment() {
        // Simulation des facteurs dynamiques (Section 8)
        double demandMultiplier = 1.0; // Peut être calculé en fonction de la demande actuelle
        double capacityMultiplier = 1.1; // Simulation d'une charge modérée
        double seasonMultiplier = 1.0; // Peut être calculé selon la date
        double weatherMultiplier = 1.0; // Facteur météo

        return demandMultiplier * capacityMultiplier * seasonMultiplier * weatherMultiplier;
    }

    private double calculateLoyaltyDiscount(double basePrice, int loyaltyPoints) {
        if (loyaltyPoints < 100) return 0;

        // Taux de réduction par niveau (équation 41)
        double discountRate = 0;
        if (loyaltyPoints >= 2500) discountRate = 0.12; // Platinum
        else if (loyaltyPoints >= 1000) discountRate = 0.08; // Gold
        else if (loyaltyPoints >= 500) discountRate = 0.05; // Silver
        else if (loyaltyPoints >= 100) discountRate = 0.02; // Bronze

        return basePrice * discountRate;
    }

    private PricingResponse buildResponse(double basePrice, double geographicCost,
                                          double temporalMultiplier, double relayPointCost,
                                          double insuranceCost, double dynamicAdjustment,
                                          double loyaltyDiscount, double finalPrice,
                                          String calculationTime) {

        PricingResponse response = new PricingResponse();

        // Pricing details
        PricingResponse.PricingDetails pricing = new PricingResponse.PricingDetails();
        pricing.setBasePrice(basePrice);
        pricing.setGeographicCost(geographicCost);
        pricing.setTemporalMultiplier(temporalMultiplier);
        pricing.setRelayPointCost(relayPointCost);
        pricing.setInsuranceCost(insuranceCost);
        pricing.setDynamicAdjustment(dynamicAdjustment);
        pricing.setLoyaltyDiscount(loyaltyDiscount);
        pricing.setFinalPrice(finalPrice);

        response.setPricing(pricing);

        // Breakdown
        Map<String, Object> breakdown = new HashMap<>();
        breakdown.put("baseCalculation", "C0 + poids_taxable * taux + carburant + manutention");
        breakdown.put("geographicCalculation", "coût_zone + coût_distance");
        breakdown.put("formula", "Ctotal = fbase(P) · fgeo(D) · ftemp(T) + frelay(R) + fins(I) + fdyn(Δ) − floy(L)");
        response.setBreakdown(breakdown);

        // Metadata
        PricingResponse.Metadata metadata = new PricingResponse.Metadata();
        metadata.setCalculationTime(calculationTime);
        metadata.setVersion("2.1.0");

        response.setMetadata(metadata);

        return response;
    }
}
