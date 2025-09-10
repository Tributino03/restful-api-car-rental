package app.dto;

import app.entity.Car;
import app.entity.Landlords;

import java.time.LocalDateTime;

public record RentalRequestDTO(
        Car car,
        Landlords landlord,
        LocalDateTime startDate,
        LocalDateTime returnDate
) {}