package com.camerexpress.pricing.controller;

import com.camerexpress.pricing.model.PricingRequest;
import com.camerexpress.pricing.model.PricingResponse;
import com.camerexpress.pricing.service.PricingCalculationService;
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
@CrossOrigin(origins = "*")
public class PricingController {

    @Autowired
    private PricingCalculationService pricingService;

    @PostMapping("/calculate")
    @Operation(summary = "Calculer le prix d'un envoi",
            description = "Calcule le prix total d'un envoi en fonction de tous les paramètres")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Calcul réussi"),
            @ApiResponse(responseCode = "400", description = "Paramètres invalides"),
            @ApiResponse(responseCode = "500", description = "Erreur interne")
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