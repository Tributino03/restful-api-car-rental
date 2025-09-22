package app.service;

import app.dto.BrandRequestDTO;
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

    public Brand create(BrandRequestDTO brandDTO) {
        fipeApiService.validateBrandNameExists(brandDTO.name());

        if (!this.brandRepository.findByName(brandDTO.name()).isEmpty()) {
            throw new IllegalArgumentException("Esta marca já está cadastrada no sistema.");
        }

        Brand newBrand = new Brand();
        newBrand.setName(brandDTO.name());
        newBrand.setFipeCode(brandDTO.fipeCode());
        newBrand.setCnpj(brandDTO.cnpj());

        return this.brandRepository.save(newBrand);
    }

    public void delete(Long id) {
        Brand brandToDelete = this.findById(id);

        List<Car> carsAssociated = this.carRepository.findByBrand(brandToDelete);
        if (!carsAssociated.isEmpty()) {
            throw new IllegalStateException("Esta marca não pode ser excluída, pois existem " + carsAssociated.size() + " carro(s) associados a ela.");
        }

        this.brandRepository.delete(brandToDelete);
    }


}