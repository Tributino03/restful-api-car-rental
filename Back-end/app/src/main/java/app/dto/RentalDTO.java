package app.dto;

import java.time.LocalDateTime;

public record RentalDTO(Long id, LocalDateTime startDate, LocalDateTime returnDate, String status) {
}