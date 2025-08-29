package app.dto;

import java.util.List;

public record CarDTO(Long id, String name, int year, BrandDTO brand, List<RentalDTO> rentals) {
}