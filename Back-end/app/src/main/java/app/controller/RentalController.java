package app.controller;

import app.dto.RentalRequestDTO;
import app.dto.RentalResponseDTO;
import app.entity.Rental;
import app.service.RentalService;
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
    public ResponseEntity<Rental> findById(@PathVariable Long id) {
        return ResponseEntity.ok(rentalService.findById(id));
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<RentalResponseDTO>> findAll() {
        return ResponseEntity.ok(rentalService.findAll());
    }

    @GetMapping("/car/{carId}")
    public ResponseEntity<List<RentalResponseDTO>> findByCarId(@PathVariable Long carId) {
        return ResponseEntity.ok(rentalService.findByCarId(carId));
    }

    @GetMapping("/landlord/{landlordId}")
    public ResponseEntity<List<RentalResponseDTO>> findByLandlordId(@PathVariable Long landlordId) {
        return ResponseEntity.ok(rentalService.findByLandlordId(landlordId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<Rental> create(@RequestBody RentalRequestDTO rentalDTO) {
        Rental newRental = rentalService.create(rentalDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newRental);
    }
}