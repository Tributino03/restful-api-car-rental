package app.service;

import app.dto.BrandDTO;
import app.dto.CarDTO;
import app.dto.CarRequestDTO;
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

    public Car create(CarRequestDTO carDTO) {
        if (carDTO.brand() == null || carDTO.brand().getId() == null) {
            throw new IllegalArgumentException("A marca do carro precisa ser selecionada.");
        }

        Brand brandFromDb = brandRepository.findById(carDTO.brand().getId())
                .orElseThrow(() -> new EntityNotFoundException("A marca com o ID " + carDTO.brand().getId() + " não foi encontrada."));

        Car newCar = new Car();
        newCar.setName(carDTO.name());
        newCar.setModelYear(carDTO.modelYear());
        newCar.setVehicleValue(carDTO.vehicleValue());
        newCar.setBrand(brandFromDb);

        fipeApiService.validateModelBelongsToBrand(newCar, brandFromDb);
        return this.carRepository.save(newCar);
    }

    public Car update(Long id, CarRequestDTO carDTO) {
        Car carFromDb = this.findById(id);

        if (carDTO.brand() == null || carDTO.brand().getId() == null) {
            throw new IllegalArgumentException("A marca do carro precisa ser selecionada.");
        }

        Brand brandFromDb = brandRepository.findById(carDTO.brand().getId())
                .orElseThrow(() -> new EntityNotFoundException("A marca com o ID " + carDTO.brand().getId() + " não foi encontrada."));

        carFromDb.setName(carDTO.name());
        carFromDb.setModelYear(carDTO.modelYear());
        carFromDb.setVehicleValue(carDTO.vehicleValue());
        carFromDb.setBrand(brandFromDb);

        fipeApiService.validateModelBelongsToBrand(carFromDb, brandFromDb);
        return this.carRepository.save(carFromDb);
    }

    public void delete(Long id) {
        Car car = this.findById(id);

        if (car.getRentals() != null && !car.getRentals().isEmpty()) {
            throw new IllegalStateException("Este carro não pode ser excluído, pois existem "
                    + car.getRentals().size() + " aluguel(is) associados a ele.");
        }

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

        return new CarDTO(car.getId(), car.getName(), car.getModelYear(), brandDTO, rentalDTOs);
    }
}