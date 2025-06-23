package pickDrop.pricing;

import pickDrop.pricing.controller.PricingController;
import pickDrop.pricing.model.PricingRequest;
import pickDrop.pricing.model.PricingResponse;
import pickDrop.pricing.service.PricingCalculationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest

// Alternative possible pour tester uniquement le contrôleur
@WebMvcTest(PricingController.class) // Ne charge que la couche Web pour ce contrôleur
public class PricingControllerTest {

	@Autowired
	private MockMvc mockMvc; // Inclus automatiquement avec @WebMvcTest

	@MockBean // Crée un mock du service et l'injecte dans le contexte
	private PricingCalculationService pricingService;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	public void testCalculatePricing() throws Exception {
		PricingRequest request = createTestRequest();
		PricingResponse mockResponse = new PricingResponse(); // Créez une fausse réponse

		// Dites à votre service mock ce qu'il doit retourner quand on l'appelle
		when(pricingService.calculatePricing(any(PricingRequest.class))).thenReturn(mockResponse);

		mockMvc.perform(post("/api/v1/pricing/calculate")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isOk());
	}

	private PricingRequest createTestRequest() {
		PricingRequest request = new PricingRequest();

		// Package info
		PricingRequest.PackageInfo packageInfo = new PricingRequest.PackageInfo();
		packageInfo.setWeight(2.5);
		packageInfo.setDeclaredValue(50000);

		PricingRequest.Dimensions dimensions = new PricingRequest.Dimensions();
		dimensions.setLength(30);
		dimensions.setWidth(20);
		dimensions.setHeight(15);
		packageInfo.setDimensions(dimensions);

		request.setPackageInfo(packageInfo);

		// Route info
		PricingRequest.RouteInfo route = new PricingRequest.RouteInfo();

		PricingRequest.Location origin = new PricingRequest.Location();
		origin.setLat(3.848);
		origin.setLng(11.502);
		origin.setCity("Yaoundé");
		origin.setZone(1);

		PricingRequest.Location destination = new PricingRequest.Location();
		destination.setLat(4.061);
		destination.setLng(9.786);
		destination.setCity("Douala");
		destination.setZone(2);

		route.setOrigin(origin);
		route.setDestination(destination);
		request.setRoute(route);

		// Service info
		PricingRequest.ServiceInfo service = new PricingRequest.ServiceInfo();
		service.setSpeed("EXPRESS");
		service.setPickupTime(LocalDateTime.now());
		service.setInsurance("STANDARD");
		service.setRelayType("STANDARD");
		service.setScheduled(false);
		request.setService(service);

		// Customer info
		PricingRequest.CustomerInfo customer = new PricingRequest.CustomerInfo();
		customer.setLoyaltyPoints(750);
		customer.setTier("SILVER");
		request.setCustomer(customer);

		return request;
	}
}

