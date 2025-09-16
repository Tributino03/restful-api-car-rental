package app.dto;

import app.entity.Brand;

public record CarRequestDTO(
        String name,
        int modelYear,
        Double vehicleValue,
        Brand brand
) {}