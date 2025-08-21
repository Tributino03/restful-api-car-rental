package app.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ViaCepDTO(String cep, String logradouro, String bairro, String localidade, String uf) {
}

