package pickDrop.pricing.controller;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import pickDrop.pricing.model.PricingRequest;
import pickDrop.pricing.model.PricingResponse;
import pickDrop.pricing.service.PricingCalculationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/pricing")
@Tag(name = "Pricing API", description = "API de calcul de tarification Pick&Drop")
public class PricingController {
    @Autowired
    private PricingCalculationService pricingService;
    // ... votre code @Autowired etc. ...

    @PostMapping("/calculate")
    @Operation(summary = "Calculer le prix d'un envoi",
            description = "Calcule le prix total d'un envoi en fonction de tous les paramètres.")

    // ================== MODIFICATION ICI ==================
    // On ne garde que l'annotation pour la réponse 200 et on la détaille
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Calcul réussi et retourné dans le corps de la réponse.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PricingResponse.class)) }
            )
            // Les annotations pour les codes 400 et 500 ont été supprimées.
    })
    public ResponseEntity<PricingResponse> calculatePricing(@Valid @RequestBody PricingRequest request) {
        try {
            PricingResponse response = pricingService.calculatePricing(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Log l'erreur et retourner une réponse d'erreur appropriée
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/health")
    @Operation(summary = "Vérifier l'état de l'API", description = "Endpoint de santé de l'API")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Pick&Drop Pricing API v2.1.0 - Status: OK");
    }
}
