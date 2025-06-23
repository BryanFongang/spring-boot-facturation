package pickDrop.pricing.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Demande de calcul de tarification")
public class PricingRequest {

    @Valid
    @NotNull
    @Schema(description = "Informations du colis")
    private PackageInfo packageInfo;

    @Valid
    @NotNull
    @Schema(description = "Informations de route")
    private RouteInfo route;

    @Valid
    @NotNull
    @Schema(description = "Type de service")
    private ServiceInfo service;

    @Valid
    @Schema(description = "Informations client")
    private CustomerInfo customer;

    // Getters et setters
    public PackageInfo getPackageInfo() { return packageInfo; }
    public void setPackageInfo(PackageInfo packageInfo) { this.packageInfo = packageInfo; }

    public RouteInfo getRoute() { return route; }
    public void setRoute(RouteInfo route) { this.route = route; }

    public ServiceInfo getService() { return service; }
    public void setService(ServiceInfo service) { this.service = service; }

    public CustomerInfo getCustomer() { return customer; }
    public void setCustomer(CustomerInfo customer) { this.customer = customer; }

    @Schema(description = "Informations du colis")
    public static class PackageInfo {
        @Positive
        @Schema(description = "Poids en kg", example = "2.5")
        private double weight;

        @Valid
        @NotNull
        private Dimensions dimensions;

        @PositiveOrZero
        @Schema(description = "Valeur déclarée en FCFA", example = "50000")
        private double declaredValue;

        public double getWeight() { return weight; }
        public void setWeight(double weight) { this.weight = weight; }

        public Dimensions getDimensions() { return dimensions; }
        public void setDimensions(Dimensions dimensions) { this.dimensions = dimensions; }

        public double getDeclaredValue() { return declaredValue; }
        public void setDeclaredValue(double declaredValue) { this.declaredValue = declaredValue; }
    }

    @Schema(description = "Dimensions du colis")
    public static class Dimensions {
        @Positive
        @Schema(description = "Longueur en cm", example = "30")
        private double length;

        @Positive
        @Schema(description = "Largeur en cm", example = "20")
        private double width;

        @Positive
        @Schema(description = "Hauteur en cm", example = "15")
        private double height;

        public double getLength() { return length; }
        public void setLength(double length) { this.length = length; }

        public double getWidth() { return width; }
        public void setWidth(double width) { this.width = width; }

        public double getHeight() { return height; }
        public void setHeight(double height) { this.height = height; }
    }

    @Schema(description = "Informations de route")
    public static class RouteInfo {
        @Valid
        @NotNull
        private Location origin;

        @Valid
        @NotNull
        private Location destination;

        public Location getOrigin() { return origin; }
        public void setOrigin(Location origin) { this.origin = origin; }

        public Location getDestination() { return destination; }
        public void setDestination(Location destination) { this.destination = destination; }
    }

    @Schema(description = "Localisation")
    public static class Location {
        @Schema(description = "Latitude", example = "3.848")
        private double lat;

        @Schema(description = "Longitude", example = "11.502")
        private double lng;

        @Schema(description = "Ville", example = "Yaoundé")
        private String city;

        @Schema(description = "Zone tarifaire (1, 2, ou 3)", example = "1")
        private int zone;

        public double getLat() { return lat; }
        public void setLat(double lat) { this.lat = lat; }

        public double getLng() { return lng; }
        public void setLng(double lng) { this.lng = lng; }

        public String getCity() { return city; }
        public void setCity(String city) { this.city = city; }

        public int getZone() { return zone; }
        public void setZone(int zone) { this.zone = zone; }
    }

    @Schema(description = "Informations de service")
    public static class ServiceInfo {
        @Schema(description = "Type de vitesse", example = "EXPRESS",
                allowableValues = {"STANDARD", "EXPRESS", "URGENT", "INSTANTANE"})
        private String speed;

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
        @Schema(description = "Heure de collecte", example = "2024-01-15T14:30:00Z")
        private LocalDateTime pickupTime;

        @Schema(description = "Type d'assurance", example = "STANDARD",
                allowableValues = {"BASIC", "STANDARD", "PREMIUM"})
        private String insurance;

        @Schema(description = "Type de point relais", example = "STANDARD",
                allowableValues = {"BASIC", "STANDARD", "PREMIUM"})
        private String relayType;

        @Schema(description = "Livraison programmée", example = "false")
        private boolean scheduled;

        public String getSpeed() { return speed; }
        public void setSpeed(String speed) { this.speed = speed; }

        public LocalDateTime getPickupTime() { return pickupTime; }
        public void setPickupTime(LocalDateTime pickupTime) { this.pickupTime = pickupTime; }

        public String getInsurance() { return insurance; }
        public void setInsurance(String insurance) { this.insurance = insurance; }

        public String getRelayType() { return relayType; }
        public void setRelayType(String relayType) { this.relayType = relayType; }

        public boolean isScheduled() { return scheduled; }
        public void setScheduled(boolean scheduled) { this.scheduled = scheduled; }
    }

    @Schema(description = "Informations client")
    public static class CustomerInfo {
        @PositiveOrZero
        @Schema(description = "Points de fidélité", example = "750")
        private int loyaltyPoints;

        @Schema(description = "Niveau de fidélité", example = "SILVER",
                allowableValues = {"BRONZE", "SILVER", "GOLD", "PLATINUM"})
        private String tier;

        public int getLoyaltyPoints() { return loyaltyPoints; }
        public void setLoyaltyPoints(int loyaltyPoints) { this.loyaltyPoints = loyaltyPoints; }

        public String getTier() { return tier; }
        public void setTier(String tier) { this.tier = tier; }
    }
}