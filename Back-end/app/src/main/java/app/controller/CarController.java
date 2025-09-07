package app.controller;

import app.dto.CarDTO;
import app.entity.Car;
import app.service.CarService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@RestController
@RequestMapping("/api/car")
public class CarController {

    @Autowired
    private CarService carService;

    @GetMapping("/findAll")
    public ResponseEntity<List<CarDTO>> findAll() {
        try {
            List<CarDTO> listCar = this.carService.findAll();
            return ResponseEntity.ok(listCar);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        try {
            Car car = this.carService.findById(id);
            return ResponseEntity.ok(car);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @GetMapping("/findByName")
    public ResponseEntity<?> findByName(@RequestParam String name) {
        try {
            List<Car> listCar = this.carService.findByName(name);
            return ResponseEntity.ok(listCar);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/findByBrand")
    public ResponseEntity<?> findByBrand(@RequestParam Long idBrand) {
        try {
            List<Car> listCar = this.carService.findByBrand(idBrand);
            return ResponseEntity.ok(listCar);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/findAboveYear")
    public ResponseEntity<?> findAboveYear(@RequestParam int year) {
        try {
            List<Car> listCar = this.carService.findAboveYear(year);
            return ResponseEntity.ok(listCar);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PreAuthorize("hasrole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody Car car) {
        try {
            Car newCar = this.carService.create(car);
            return ResponseEntity.status(HttpStatus.CREATED).body(newCar);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PreAuthorize("hasrole('ADMIN')")
    @PutMapping("/update/{id}") // URL ajustada para o padrão
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Car car){
        try {
            Car updatedCar = this.carService.update(id, car);
            return ResponseEntity.ok(updatedCar);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PreAuthorize("hasrole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        try {
            this.carService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null); // Corpo vazio para erros
        }
    }
}