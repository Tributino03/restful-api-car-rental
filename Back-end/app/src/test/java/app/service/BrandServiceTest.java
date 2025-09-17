package app.service;

import app.entity.Brand;
import app.repository.BrandRepository;
import app.repository.CarRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    }
