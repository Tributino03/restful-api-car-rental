package app.dto;

import java.util.List;

public record CarDTO(Long id, String name, int modelYear, Double vehicleValue, BrandDTO brand, List<RentalDTO> rentals) {

}
