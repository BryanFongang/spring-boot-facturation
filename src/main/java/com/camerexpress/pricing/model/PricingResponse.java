package com.camerexpress.pricing.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Map;

@Schema(description = "Réponse de calcul de tarification")
public class PricingResponse {

    @Schema(description = "Détails de tarification")
    private PricingDetails pricing;

    @Schema(description = "Détail des calculs")
    private Map<String, Object> breakdown;

    @Schema(description = "Métadonnées")
    private Metadata metadata;

    public PricingDetails getPricing() { return pricing; }
    public void setPricing(PricingDetails pricing) { this.pricing = pricing; }

    public Map<String, Object> getBreakdown() { return breakdown; }
    public void setBreakdown(Map<String, Object> breakdown) { this.breakdown = breakdown; }

    public Metadata getMetadata() { return metadata; }
    public void setMetadata(Metadata metadata) { this.metadata = metadata; }

    @Schema(description = "Détails de tarification")
    public static class PricingDetails {
        @Schema(description = "Prix de base", example = "1500.0")
        private double basePrice;

        @Schema(description = "Coût géographique", example = "800.0")
        private double geographicCost;

        @Schema(description = "Multiplicateur temporel", example = "1.5")
        private double temporalMultiplier;

        @Schema(description = "Coût point relais", example = "250.0")
        private double relayPointCost;

        @Schema(description = "Coût assurance", example = "300.0")
        private double insuranceCost;

        @Schema(description = "Ajustement dynamique", example = "1.2")
        private double dynamicAdjustment;

        @Schema(description = "Remise fidélité", example = "150.0")
        private double loyaltyDiscount;

        @Schema(description = "Prix final", example = "3200.0")
        private double finalPrice;

        // Getters et setters
        public double getBasePrice() { return basePrice; }
        public void setBasePrice(double basePrice) { this.basePrice = basePrice; }

        public double getGeographicCost() { return geographicCost; }
        public void setGeographicCost(double geographicCost) { this.geographicCost = geographicCost; }

        public double getTemporalMultiplier() { return temporalMultiplier; }
        public void setTemporalMultiplier(double temporalMultiplier) { this.temporalMultiplier = temporalMultiplier; }

        public double getRelayPointCost() { return relayPointCost; }
        public void setRelayPointCost(double relayPointCost) { this.relayPointCost = relayPointCost; }

        public double getInsuranceCost() { return insuranceCost; }
        public void setInsuranceCost(double insuranceCost) { this.insuranceCost = insuranceCost; }

        public double getDynamicAdjustment() { return dynamicAdjustment; }
        public void setDynamicAdjustment(double dynamicAdjustment) { this.dynamicAdjustment = dynamicAdjustment; }

        public double getLoyaltyDiscount() { return loyaltyDiscount; }
        public void setLoyaltyDiscount(double loyaltyDiscount) { this.loyaltyDiscount = loyaltyDiscount; }

        public double getFinalPrice() { return finalPrice; }
        public void setFinalPrice(double finalPrice) { this.finalPrice = finalPrice; }
    }

    @Schema(description = "Métadonnées de calcul")
    public static class Metadata {
        @Schema(description = "Temps de calcul", example = "45ms")
        private String calculationTime;

        @Schema(description = "Version de l'API", example = "2.1.0")
        private String version;

        public String getCalculationTime() { return calculationTime; }
        public void setCalculationTime(String calculationTime) { this.calculationTime = calculationTime; }

        public String getVersion() { return version; }
        public void setVersion(String version) { this.version = version; }
    }
}