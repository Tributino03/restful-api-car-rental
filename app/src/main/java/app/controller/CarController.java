package app.controller;

import app.entity.Car;
import app.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/car")
public class CarController {

    @Autowired
    private CarService carService;

    @GetMapping("/findAll")
    public ResponseEntity<List<Car>> findAll() {
        try {
            List<Car> listCar = this.carService.findAll();
            return new ResponseEntity<>(listCar, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<Car> findById(@PathVariable Long id) {
        try {
            Car car = this.carService.findById(id);
            return new ResponseEntity<>(car, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/findByName")
    public ResponseEntity<List<Car>> findByName(@RequestParam String name) {
        try {
            List<Car> listCar = this.carService.findByName(name);
            return new ResponseEntity<>(listCar, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/findByBrand")
    public ResponseEntity<List<Car>> findByBrand(@RequestParam Long idBrand) {
        try {
            List<Car> listCar = this.carService.findByBrand(idBrand);
            return new ResponseEntity<>(listCar, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/findAboveYear")
    public ResponseEntity<List<Car>> findAboveYear(@RequestParam int year) {
        try {
            List<Car> listCar = this.carService.findAboveYear(year);
            return new ResponseEntity<>(listCar, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestBody Car car) {
        try {
            String mensagem = this.carService.create(car);
            return new ResponseEntity<>(mensagem, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> update(@RequestBody Car car, @PathVariable Long id){
        try {
            String mensagem = this.carService.update(car, id);
            return new ResponseEntity<>(mensagem, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id){
        try {
            String mensagem = this.carService.delete(id);
            return new ResponseEntity<>(mensagem, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        }
    }
}
