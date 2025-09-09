package app.dto;

public record BrandRequestDTO(
        String name,
        Long fipeCode,
        String cnpj
) {}