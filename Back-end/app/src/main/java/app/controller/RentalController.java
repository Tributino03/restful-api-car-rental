package app.controller;

import app.dto.RentalResponseDTO;
import app.entity.Rental;
import app.service.RentalService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rental")
public class RentalController {

    @Autowired
    private RentalService rentalService;

    @GetMapping("/findById/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        try {
            Rental rental = rentalService.findById(id);
            return ResponseEntity.ok(rental);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<RentalResponseDTO>> findAll() {
        return ResponseEntity.ok(rentalService.findAll());
    }

    @GetMapping("/car/{carId}")
    public ResponseEntity<?> findByCarId(@PathVariable Long carId) {
        try {
            List<RentalResponseDTO> rentals = rentalService.findByCarId(carId);
            return ResponseEntity.ok(rentals);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/landlord/{landlordId}")
    public ResponseEntity<?> findByLandlordId(@PathVariable Long landlordId) {
        try {
            List<RentalResponseDTO> rentals = rentalService.findByLandlordId(landlordId);
            return ResponseEntity.ok(rentals);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PreAuthorize("hasrole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody Rental rental) {
        try {
            Rental newRental = rentalService.create(rental);
            return ResponseEntity.status(HttpStatus.CREATED).body(newRental);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}