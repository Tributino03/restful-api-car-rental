package app.service;

import app.dto.FipeBrandDTO;
import app.dto.ModelosApiResponseDTO;
import app.entity.Brand;
import app.entity.Car;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class FipeApiService {

    @Autowired
    private WebClient webClient;

    public void validateBrandNameExists(String brandName) {
        List<FipeBrandDTO> brandsFromApi = webClient.get()
                .uri("/carros/marcas")
                .retrieve()
                .bodyToFlux(FipeBrandDTO.class)
                .collectList()
                .block();

        if (brandsFromApi == null) {
            throw new IllegalStateException("A resposta da API FIPE foi nula.");
        }

        boolean exists = brandsFromApi.stream()
                .anyMatch(apiBrand ->
                        apiBrand.name() != null &&
                                apiBrand.name().toLowerCase().contains(brandName.toLowerCase())
                );

        if (!exists) {
            throw new IllegalArgumentException("Validação FIPE falhou: A marca '" + brandName + "' não é uma marca de carro válida.");
        }
    }

    public void validateModelBelongsToBrand(Car car, Brand brand) {
        String uri = String.format("/carros/marcas/%d/modelos", brand.getFipeCode());

        ModelosApiResponseDTO apiResponse = webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(ModelosApiResponseDTO.class)
                .block();

        if (apiResponse == null || apiResponse.modelos() == null) {
            throw new IllegalArgumentException("Não foi possível encontrar modelos para a marca: " + brand.getName());
        }

        boolean modelExistsInBrand = apiResponse.modelos().stream()
                .anyMatch(modelo ->
                        modelo.nome() != null &&
                                car.getName().trim().equalsIgnoreCase(modelo.nome().trim())
                );

        if (!modelExistsInBrand) {
            throw new IllegalArgumentException("Validação FIPE falhou: O modelo '" + car.getName() + "' não pertence à marca '" + brand.getName() + "'.");
        }
    }
}