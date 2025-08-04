package app.service;

import app.entity.Brand;
import app.entity.Car;
import app.repository.CarRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarService {

    @Autowired
    private CarRepository carRepository;

    public List<Car> findAll(){
        return this.carRepository.findAll();
    }

    public Car findById(Long id){
        return this.carRepository.findById(id).get();
    }

    public List<Car> findByName(String name){
        return this.carRepository.findByName(name);
    }

    public List<Car> findByBrand(Long idBrand){
        Brand brand = new Brand();
        brand.setId(idBrand);
        return this.carRepository.findByBrand(brand);
    }

    public List<Car> findAboveYear(int year){
        return this.carRepository.findAboveYear(year);
    }

    public String create(Car car){
        this.carRepository.save(car);
        return "Car registered successfully.";
    }

    public String update(Car car, Long id){
        car.setId(id);
        this.carRepository.save(car);
        return "Car updated successfully";
    }

    public String delete(Long id){
        this.carRepository.deleteById(id);
        return "Car deleted successfully";
    }
}
