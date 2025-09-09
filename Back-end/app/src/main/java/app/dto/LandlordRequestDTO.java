package app.dto;

import app.entity.Address;
import java.time.LocalDate;

public record LandlordRequestDTO(
        String name,
        LocalDate dateOfBirth,
        String cpf,
        Address address
) {}