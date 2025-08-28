package app.controller;

import app.dto.ViaCepDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
@RequestMapping("/api/address")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class AddressController {

    private final WebClient webClient;

    public AddressController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://viacep.com.br/ws").build();
    }

    @GetMapping("/cep/{cep}")
    public ResponseEntity<ViaCepDTO> findByCep(@PathVariable String cep) {
        try {
            ViaCepDTO address = this.webClient.get()
                    .uri("/{cep}/json/", cep)
                    .retrieve()
                    .bodyToMono(ViaCepDTO.class)
                    .block();
            return ResponseEntity.ok(address);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}