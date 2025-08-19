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
public class BrandService {

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private FipeApiService fipeApiService;

    public List<Brand> findAll() {
        return brandRepository.findAll();
    }

    public Brand findById(Long id) {
        return brandRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Marca não encontrada com o id: " + id));
    }

    public List<Brand> findByName(String name) {
        return brandRepository.findByName(name);
    }

    public String create(Brand brand) {
        fipeApiService.validateBrandNameExists(brand.getName());

        if (!this.brandRepository.findByName(brand.getName()).isEmpty()) {
            throw new IllegalArgumentException("Esta marca já está cadastrada no sistema.");
        }

        this.brandRepository.save(brand);
        return "Marca cadastrada com sucesso";
    }

    public String delete(Long id) {
        Brand brandToDelete = this.findById(id);

        List<Car> carsAssociated = this.carRepository.findByBrand(brandToDelete);
        if (!carsAssociated.isEmpty()) {
            throw new IllegalStateException("Esta marca não pode ser excluída, pois existem " + carsAssociated.size() + " carro(s) associados a ela.");
        }

        this.brandRepository.delete(brandToDelete);
        return "Marca deletada com sucesso";
    }
}