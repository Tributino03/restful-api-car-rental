package app.service;

import app.dto.BrandRequestDTO;
import app.entity.Brand;
import app.entity.Car;
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
class BrandServiceTest {

    @Mock
    private CarRepository carRepository;

    @Mock
    private BrandRepository brandRepository;

    @Mock
    private FipeApiService fipeApiService;

    @InjectMocks
    private BrandService brandService;

    @Test
    @DisplayName("Deve encontrar a marca com o id correspondente")
    void findByIdCase1() {
        Brand brand = new Brand();
        brand.setId(1L);
        when(brandRepository.findById(1L)).thenReturn(Optional.of(brand));

        Brand result = brandService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(brandRepository, times(1)).findById(1L);

    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar um ID de uma marca que não existe")
    void findByIdCase2() {
        when(brandRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> brandService.findById(99L));
    }

    @Test
    @DisplayName("Deve criar uma marca com sucesso")
    void createCase1() {
        BrandRequestDTO brandRequestDTO = new BrandRequestDTO("Fiat", 21L, "11.222.333/0001-44");
        when(brandRepository.findByName("Fiat")).thenReturn(List.of());
        doNothing().when(fipeApiService).validateBrandNameExists("Fiat");
        when(brandRepository.save(any(Brand.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Brand result = brandService.create(brandRequestDTO);

        assertNotNull(result);
        assertEquals("Fiat", result.getName());
        verify(brandRepository, times(1)).save(any(Brand.class));
    }


    @Test
    @DisplayName("Deve lançar exceção ao tentar criar uma marca que já existe no banco")
    void createCase2() {
        BrandRequestDTO brandRequestDTO = new BrandRequestDTO("Ford", 22L, "22.333.444/0001-55");
        doNothing().when(fipeApiService).validateBrandNameExists("Ford");
        when(brandRepository.findByName("Ford")).thenReturn(List.of(new Brand()));

        assertThrows(IllegalArgumentException.class, () -> brandService.create(brandRequestDTO));
        verify(brandRepository, never()).save(any(Brand.class));
    }


    @Test
    @DisplayName("Deve lançar exceção quando a validação FIPE falha")
    void createCase3() {
        BrandRequestDTO brandRequestDTO = new BrandRequestDTO("MarcaInvalida", 999L, "00.000.000/0000-00");
        doThrow(new IllegalArgumentException("Marca inválida"))
                .when(fipeApiService).validateBrandNameExists("MarcaInvalida");

        assertThrows(IllegalArgumentException.class, () -> brandService.create(brandRequestDTO));
        verify(brandRepository, never()).save(any(Brand.class));
    }


    @Test
    @DisplayName("Deve deletar uma marca com sucesso se não houver carros associados a ela")
    void deleteCase1(){
        Brand brand = new Brand();
        brand.setId(1L);
        brand.setCars(new ArrayList<>());

        when(brandRepository.findById(1L)).thenReturn(Optional.of(brand));
        doNothing().when(brandRepository).delete(brand);

        assertDoesNotThrow(() -> brandService.delete(1L));
        verify(brandRepository, times(1)).delete(brand);
    }

    @Test
    @DisplayName("Deve lancar uma exececao ao tentar deletar uma marca que tem carros associados a ela")
    void deleteCase2(){

        Brand brandComCarros = new Brand();
        brandComCarros.setId(1L);

        List<Car> listaDeCarrosAssociados = List.of(new Car());

        when(brandRepository.findById(1L)).thenReturn(Optional.of(brandComCarros));

        when(carRepository.findByBrand(brandComCarros)).thenReturn(listaDeCarrosAssociados);

        assertThrows(IllegalStateException.class, () -> {
            brandService.delete(1L);
        });

        verify(brandRepository, never()).delete(any(Brand.class));
    }

    }
