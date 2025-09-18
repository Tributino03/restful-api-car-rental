package app.service;

import app.dto.FipeBrandDTO;
import app.dto.ModeloDTO;
import app.dto.ModelosApiResponseDTO;
import app.entity.Brand;
import app.entity.Car;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class FipeApiServiceTest {

    public static MockWebServer mockWebServer;
    private FipeApiService fipeApiService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    static void setUpServer() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterAll
    static void tearDownServer() throws IOException {
        mockWebServer.shutdown();
    }

    @BeforeEach
    void initialize() {
        String baseUrl = String.format("http://localhost:%s", mockWebServer.getPort());
        WebClient webClient = WebClient.builder().baseUrl(baseUrl).build();
        fipeApiService = new FipeApiService(webClient);
    }

    @Test
    @DisplayName("Deve passar na validação quando a marca existe na API")
    void validateBrandNameExistsCase1() throws Exception {
        FipeBrandDTO brand = new FipeBrandDTO("Ford", "22");
        String jsonResponse = objectMapper.writeValueAsString(List.of(brand));

        mockWebServer.enqueue(new MockResponse()
                .setBody(jsonResponse)
                .addHeader("Content-Type", "application/json"));

        assertDoesNotThrow(() -> fipeApiService.validateBrandNameExists("Ford"));
    }

    @Test
    @DisplayName("Deve lançar exceção quando a marca não existe na API")
    void validateBrandNameExistsCase2() {

        mockWebServer.enqueue(new MockResponse()
                .setBody("[]")
                .addHeader("Content-Type", "application/json"));

        assertThrows(IllegalArgumentException.class, () -> fipeApiService.validateBrandNameExists("MarcaInvalida"));
    }

    @Test
    @DisplayName("Deve passar na validação quando o modelo pertence à marca")
    void validateModelBelongsToBrandCase1() throws Exception {
        ModeloDTO modelo = new ModeloDTO("Fiesta", 123);
        ModelosApiResponseDTO response = new ModelosApiResponseDTO(List.of(modelo));
        String jsonResponse = objectMapper.writeValueAsString(response);

        mockWebServer.enqueue(new MockResponse()
                .setBody(jsonResponse)
                .addHeader("Content-Type", "application/json"));

        Car car = new Car();
        car.setName("Fiesta");
        Brand brand = new Brand();
        brand.setFipeCode(22L);

        assertDoesNotThrow(() -> fipeApiService.validateModelBelongsToBrand(car, brand));
    }

    @Test
    @DisplayName("Deve lançar exceção quando o modelo não pertence à marca")
    void validateModelBelongsToBrandCase2() throws Exception {
        ModeloDTO modelo = new ModeloDTO("Fiesta", 123);
        ModelosApiResponseDTO response = new ModelosApiResponseDTO(List.of(modelo));
        String jsonResponse = objectMapper.writeValueAsString(response);

        mockWebServer.enqueue(new MockResponse()
                .setBody(jsonResponse)
                .addHeader("Content-Type", "application/json"));

        Car car = new Car();
        car.setName("Onix");
        Brand brand = new Brand();
        brand.setFipeCode(22L);
        brand.setName("Ford");

        assertThrows(IllegalArgumentException.class, () -> fipeApiService.validateModelBelongsToBrand(car, brand));
    }
}