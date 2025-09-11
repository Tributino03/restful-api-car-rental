package app.controller;

import app.dto.BrandRequestDTO;
import app.entity.Brand;
import app.service.BrandService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/brand")
public class BrandController {

    @Autowired
    private BrandService brandService;

    @GetMapping("/findAll")
    public ResponseEntity<List<Brand>> findAll() {
        return ResponseEntity.ok(brandService.findAll());
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<Brand> findById(@PathVariable Long id) {
        return ResponseEntity.ok(brandService.findById(id));
    }

    @GetMapping("/findByName")
    public ResponseEntity<List<Brand>> findByName(@RequestParam String name) {
        return ResponseEntity.ok(brandService.findByName(name));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<Brand> create(@Valid @RequestBody BrandRequestDTO brandDTO) {
        Brand newBrand = brandService.create(brandDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newBrand);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        brandService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
