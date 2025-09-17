package app.service;

import app.dto.CarRequestDTO;
import app.entity.Brand;
import app.entity.Car;
import app.entity.Rental;
import app.repository.BrandRepository;
import app.repository.CarRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarServiceTest {

    @Mock
    private CarRepository carRepository;

    @Mock
    private BrandRepository brandRepository;

    @Mock
    private FipeApiService fipeApiService;

    @InjectMocks
    private CarService carService;


    @Test
    @DisplayName("Deve criar um carro com sucesso quando todos os dados são válidos")
    void createCase1() {
        Brand brand = new Brand();
        brand.setId(1L);
        CarRequestDTO carDTO = new CarRequestDTO("Fiesta", 2020, 90000.00, brand);

        when(brandRepository.findById(1L)).thenReturn(Optional.of(brand));
        doNothing().when(fipeApiService).validateModelBelongsToBrand(any(Car.class), any(Brand.class));
        when(carRepository.save(any(Car.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Car result = carService.create(carDTO);

        assertNotNull(result);
        assertEquals("Fiesta", result.getName());
        verify(carRepository, times(1)).save(any(Car.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar criar um carro com uma marca que não existe")
    void createCase2() {
        Brand brand = new Brand();
        brand.setId(99L);
        CarRequestDTO carDTO = new CarRequestDTO("Fusca", 1980, 20000.00, brand);

        when(brandRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> carService.create(carDTO));
        verify(carRepository, never()).save(any(Car.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando a validação FIPE falha")
    void createCase3() {
        Brand brand = new Brand();
        brand.setId(1L);
        brand.setName("Ford");
        CarRequestDTO carDTO = new CarRequestDTO("Onix", 2020, 90000.00, brand);

        when(brandRepository.findById(1L)).thenReturn(Optional.of(brand));
        doThrow(new IllegalArgumentException("Modelo não pertence à marca"))
                .when(fipeApiService).validateModelBelongsToBrand(any(Car.class), any(Brand.class));

        assertThrows(IllegalArgumentException.class, () -> carService.create(carDTO));
        verify(carRepository, never()).save(any(Car.class));
    }

    @Test
    @DisplayName("Deve encontrar um carro pelo ID com sucesso")
    void findByIdCase1() {
        Car car = new Car();
        car.setId(1L);
        when(carRepository.findById(1L)).thenReturn(Optional.of(car));

        Car result = carService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(carRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar um ID de carro que não existe")
    void findByIdCase2() {
        when(carRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> carService.findById(99L));
    }

    @Test
    @DisplayName("Deve atualizar um carro com sucesso")
    void updateCase1() {
        Brand brand = new Brand();
        brand.setId(1L);
        CarRequestDTO carDTO = new CarRequestDTO("Novo Fiesta", 2021, 95000.00, brand);

        Car carExistente = new Car();
        carExistente.setId(1L);

        when(carRepository.findById(1L)).thenReturn(Optional.of(carExistente));
        when(brandRepository.findById(1L)).thenReturn(Optional.of(brand));
        when(carRepository.save(any(Car.class))).thenReturn(carExistente);

        Car result = carService.update(1L, carDTO);

        assertNotNull(result);
        assertEquals("Novo Fiesta", result.getName());
        assertEquals(2021, result.getModelYear());
        verify(carRepository, times(1)).save(any(Car.class));
    }

    @Test
    @DisplayName("Deve deletar um carro com sucesso se não houver aluguéis")
    void deleteCase1() {
        Car car = new Car();
        car.setId(1L);
        car.setRentals(new ArrayList<>());
        when(carRepository.findById(1L)).thenReturn(Optional.of(car));
        doNothing().when(carRepository).delete(car);

        assertDoesNotThrow(() -> carService.delete(1L));
        verify(carRepository, times(1)).delete(car);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar deletar um carro com aluguéis associados")
    void deleteCase2() {
        Car car = new Car();
        car.setId(1L);
        car.setRentals(List.of(new Rental()));
        when(carRepository.findById(1L)).thenReturn(Optional.of(car));

        assertThrows(IllegalStateException.class, () -> carService.delete(1L));
        verify(carRepository, never()).delete(any(Car.class));
    }
}