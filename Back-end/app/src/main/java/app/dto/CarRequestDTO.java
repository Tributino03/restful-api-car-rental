package app.dto;

import app.entity.Brand;

public record CarRequestDTO(
        String name,
        int year,
        Double vehicleValue,
        Brand brand
) {}