package app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
public record FipeBrandDTO(
        @JsonProperty("nome") String name,
        @JsonProperty("codigo") String code
) {}