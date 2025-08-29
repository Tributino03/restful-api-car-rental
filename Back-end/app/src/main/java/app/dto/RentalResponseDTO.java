package app.dto;

import java.time.LocalDateTime;

public record RentalResponseDTO(
        Long id,
        LocalDateTime startDate,
        LocalDateTime returnDate,
        Double totalValue,
        String status,
        CarSimpleDTO car,
        LandlordSimpleDTO landlord
) {}