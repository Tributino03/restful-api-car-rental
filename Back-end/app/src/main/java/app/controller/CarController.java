package app.controller;

import app.entity.Car;
import app.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@RestController
@RequestMapping("/api/car")
@CrossOrigin(origins = "*")
public class CarController {

    @Autowired
    private CarService carService;

    @GetMapping("/findAll")
    public ResponseEntity<?> findAll() {
        try {
            List<Car> listCar = this.carService.findAll();
            return ResponseEntity.ok(listCar);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        try {
            Car car = this.carService.findById(id);
            return ResponseEntity.ok(car);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
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

    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestBody Car car) {
        try {
            String mensagem = this.carService.create(car);
            return ResponseEntity.status(HttpStatus.CREATED).body(mensagem);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> update(@RequestBody Car car, @PathVariable Long id){
        try {
            String mensagem = this.carService.update(id, car);
            return ResponseEntity.ok(mensagem);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id){
        try {
            String mensagem = this.carService.delete(id);
            return ResponseEntity.ok(mensagem);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}