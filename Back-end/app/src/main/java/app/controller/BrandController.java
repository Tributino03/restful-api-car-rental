package app.controller;

import app.entity.Brand;
import app.entity.Car;
import app.service.BrandService; // Importe o BrandService
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/brand")
@CrossOrigin(origins = "*")
public class BrandController {

    @Autowired
    private BrandService brandService;

    @GetMapping("/findAll")
    public  ResponseEntity<?> findAll(){
        try {
            List<Brand> listBrand = brandService.findAll();
            return ResponseEntity.ok(listBrand);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        try {
            Brand brand = this.brandService.findById(id);
            return ResponseEntity.ok(brand);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao buscar a marca: " + e.getMessage());
        }
    }

    @GetMapping("/findByName")
    public ResponseEntity<List<Brand>> findByName(@RequestParam String name) {
        try {
            List<Brand> lista = this.brandService.findByName(name);
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestBody Brand brand) {
        try {
            String mensagem = this.brandService.create(brand);
            return ResponseEntity.status(HttpStatus.CREATED).body(mensagem);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocorreu um erro inesperado no servidor.");
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        try {
            String mensagem = this.brandService.delete(id);
            return ResponseEntity.ok(mensagem);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}