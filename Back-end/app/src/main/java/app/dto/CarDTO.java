package app.dto;

import java.util.List;

public record CarDTO(Long id, String name, int modelYear, BrandDTO brand, List<RentalDTO> rentals) {
}