package app.service;

import app.dto.BrandDTO;
import app.dto.CarDTO;
import app.dto.RentalDTO;
import app.entity.Brand;
import app.entity.Car;
import app.repository.BrandRepository;
import app.repository.CarRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarService {

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private FipeApiService fipeApiService;

    @Autowired
    private BrandRepository brandRepository;

    public List<CarDTO> findAll() {
        List<Car> cars = carRepository.findAll();
        return cars.stream().map(this::convertToDTO).collect(Collectors.toList());
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

    public Car create(Car car) {
        if (car.getBrand() == null || car.getBrand().getId() == null || car.getBrand().getId() == 0) {
            throw new IllegalArgumentException("A marca do carro precisa ser selecionada.");
        }

        Brand brandFromDb = brandRepository.findById(car.getBrand().getId())
                .orElseThrow(() -> new EntityNotFoundException("A marca com o ID " + car.getBrand().getId() + " não foi encontrada no banco."));

        car.setBrand(brandFromDb);
        fipeApiService.validateModelBelongsToBrand(car, brandFromDb);

        return this.carRepository.save(car);
    }

    public Car update(Long id, Car carDetails) {
        Car carFromDb = this.findById(id);

        Brand brandFromDb = brandRepository.findById(carDetails.getBrand().getId())
                .orElseThrow(() -> new EntityNotFoundException("A marca com o ID " + carDetails.getBrand().getId() + " não foi encontrada no banco."));

        fipeApiService.validateModelBelongsToBrand(carDetails, brandFromDb);

        carFromDb.setName(carDetails.getName());
        carFromDb.setYear(carDetails.getYear());
        carFromDb.setVehicleValue(carDetails.getVehicleValue());
        carFromDb.setBrand(brandFromDb);
        return this.carRepository.save(carFromDb);
    }

    public void delete(Long id){
        Car car = this.findById(id);
        this.carRepository.delete(car);
    }

    private CarDTO convertToDTO(Car car) {
        BrandDTO brandDTO = null;
        if (car.getBrand() != null) {
            brandDTO = new BrandDTO(car.getBrand().getId(), car.getBrand().getName(), car.getBrand().getCnpj());
        }

        List<RentalDTO> rentalDTOs = (car.getRentals() != null) ? car.getRentals().stream()
                .map(rental -> new RentalDTO(rental.getId(), rental.getStartDate(), rental.getReturnDate(), rental.getStatus()))
                .collect(Collectors.toList()) : Collections.emptyList();

        return new CarDTO(car.getId(), car.getName(), car.getYear(), brandDTO, rentalDTOs);
    }
}