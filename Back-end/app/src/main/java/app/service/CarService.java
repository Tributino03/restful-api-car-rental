package app.service;

import app.entity.Brand;
import app.entity.Car;
import app.repository.BrandRepository;
import app.repository.CarRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarService {

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private FipeApiService fipeApiService;

    @Autowired
    private BrandRepository brandRepository;

    public List<Car> findAll(){
        return this.carRepository.findAll();
    }

    public Car findById(Long id){
        return this.carRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Carro não encontrado com o id: " + id));
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

    public String create(Car car) {
        Brand brandFromDb = brandRepository.findById(car.getBrand().getId())
                .orElseThrow(() -> new EntityNotFoundException("A marca com o ID " + car.getBrand().getId() + " não foi encontrada no banco."));

        car.setBrand(brandFromDb);

        fipeApiService.validateModelBelongsToBrand(car, brandFromDb);

        this.carRepository.save(car);
        return "Car registered successfully.";
    }

    public String update(Long id, Car car){
        Car carUpdate = this.findById(id);

        carUpdate.setName(car.getName());
        carUpdate.setYear(car.getYear());
        carUpdate.setBrand(car.getBrand());

        this.carRepository.save(carUpdate);

        return "Car updated successfully";
    }

    public String delete(Long id){
        Car car = this.findById(id);
        this.carRepository.delete(car);
        return "Car deleted successfully";
    }
}
