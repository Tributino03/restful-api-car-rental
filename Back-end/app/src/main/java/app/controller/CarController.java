package app.controller;

import app.dto.CarDTO;
import app.dto.CarRequestDTO;
import app.entity.Car;
import app.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/car")
public class CarController {

    @Autowired
    private CarService carService;

    @GetMapping("/findAll")
    public ResponseEntity<List<CarDTO>> findAll() {
        return ResponseEntity.ok(carService.findAll());
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<Car> findById(@PathVariable Long id) {
        return ResponseEntity.ok(carService.findById(id));
    }

    @GetMapping("/findByName")
    public ResponseEntity<List<Car>> findByName(@RequestParam String name) {
        return ResponseEntity.ok(carService.findByName(name));
    }

    @GetMapping("/findByBrand")
    public ResponseEntity<List<Car>> findByBrand(@RequestParam Long idBrand) {
        return ResponseEntity.ok(carService.findByBrand(idBrand));
    }

    @GetMapping("/findAboveYear")
    public ResponseEntity<List<Car>> findAboveYear(@RequestParam int year) {
        return ResponseEntity.ok(carService.findAboveYear(year));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<Car> create(@RequestBody CarRequestDTO carDTO) {
        Car newCar = carService.create(carDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCar);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<Car> update(@PathVariable Long id, @RequestBody CarRequestDTO carDTO) {
        return ResponseEntity.ok(carService.update(id, carDTO));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        carService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
