package app.controller;

import app.dto.LandlordRequestDTO;
import app.entity.Address;
import app.entity.Landlords;
import app.service.LandlordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/landlord")
public class LandlordController {

    @Autowired
    private LandlordService landlordService;

    @GetMapping("/findById/{id}")
    public ResponseEntity<Landlords> findById(@PathVariable Long id) {
        return ResponseEntity.ok(landlordService.findById(id));
    }

    @GetMapping("/findByCpf")
    public ResponseEntity<Landlords> findByCpf(@RequestParam String cpf) {
        return ResponseEntity.ok(landlordService.findByCpf(cpf));
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<Landlords>> findAll() {
        return ResponseEntity.ok(landlordService.findAll());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<Landlords> create(@RequestBody LandlordRequestDTO landlordDTO) {
        Landlords newLandlord = landlordService.create(landlordDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newLandlord);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/updateAddress/{id}")
    public ResponseEntity<Landlords> updateAddress(@PathVariable Long id, @RequestBody Address addressDetails) {
        return ResponseEntity.ok(landlordService.updateAddress(id, addressDetails));
    }
}