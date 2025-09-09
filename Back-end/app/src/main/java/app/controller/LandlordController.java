package app.controller;

import app.dto.LandlordRequestDTO;
import app.entity.Address;
import app.entity.Car;
import app.entity.Landlords;
import app.service.LandlordService;
import jakarta.persistence.EntityNotFoundException;
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
    LandlordService landlordService;

    @GetMapping("/findById/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id){
        try{
            Landlords landlords = this.landlordService.findById(id);
            return ResponseEntity.ok(landlords);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/findByCpf")
    public ResponseEntity<?> findByCpf(@RequestParam String cpf) {
        try {
            Landlords landlord = this.landlordService.findByCpf(cpf);
            return ResponseEntity.ok(landlord);
        } catch (jakarta.persistence.EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao buscar locador por CPF: " + e.getMessage());
        }

    }

    @GetMapping("/findAll")
    public ResponseEntity<?> findAll() {
        try {
            List<Landlords> landlords = this.landlordService.findAll();
            return ResponseEntity.ok(landlords);
        } catch (jakarta.persistence.EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PreAuthorize("hasrole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody LandlordRequestDTO landlordDTO) { // <-- Recebe DTO
        try {
            Landlords newLandlord = this.landlordService.create(landlordDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(newLandlord);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PreAuthorize("hasrole('ADMIN')")
    @PutMapping("/updateAddress/{id}")
    public ResponseEntity<?> updateAddress(@PathVariable Long id, @RequestBody Address addressDetails) {
        try {
            Landlords updatedLandlord = this.landlordService.updateAddress(id, addressDetails);
            return ResponseEntity.ok(updatedLandlord);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao atualizar o endereço: " + e.getMessage());
        }
    }


}