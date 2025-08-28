package app.controller;

import app.entity.Brand;
import app.service.BrandService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/brand")
public class BrandController {

    @Autowired
    private BrandService brandService;

    @GetMapping("/findAll")
    public ResponseEntity<List<Brand>> findAll() {
        try {
            List<Brand> listBrand = brandService.findAll();
            return ResponseEntity.ok(listBrand);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        try {
            Brand brand = this.brandService.findById(id);
            return ResponseEntity.ok(brand);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/findByName")
    public ResponseEntity<List<Brand>> findByName(@RequestParam String name) {
        return ResponseEntity.ok(this.brandService.findByName(name));
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody Brand brand) {
        try {
            Brand newBrand = this.brandService.create(brand);
            return ResponseEntity.status(HttpStatus.CREATED).body(newBrand);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            this.brandService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}