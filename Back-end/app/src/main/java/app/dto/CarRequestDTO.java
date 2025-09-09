package app.dto;

public record CarRequestDTO(
        String name,
        int year,
        Double vehicleValue,
        Long brandId
) {}