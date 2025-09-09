package app.dto;

import java.time.LocalDateTime;

public record RentalRequestDTO(
        Long carId,
        Long landlordId,
        LocalDateTime startDate,
        LocalDateTime returnDate
) {}