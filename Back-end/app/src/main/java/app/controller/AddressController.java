package app.controller;

import app.dto.ViaCepDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
@RequestMapping("/api/address")
public class AddressController {

    private final WebClient webClient;

    public AddressController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://viacep.com.br/ws").build();
    }

    @GetMapping("/cep/{cep}")
    public ResponseEntity<ViaCepDTO> findByCep(@PathVariable String cep) {
        try {
            String cleanCep = cep.replaceAll("\\D", "");

            if (cleanCep.length() == 7) {
                cleanCep = "0" + cleanCep;
            }

            if (cleanCep.length() != 8) {
                return ResponseEntity.badRequest().build();
            }

            ViaCepDTO address = this.webClient.get()
                    .uri("/{cep}/json/", cleanCep)
                    .retrieve()
                    .bodyToMono(ViaCepDTO.class)
                    .block();

            return ResponseEntity.ok(address);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}