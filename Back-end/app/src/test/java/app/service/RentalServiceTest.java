package app.service;

import app.dto.RentalDTO;
import app.dto.RentalRequestDTO;
import app.dto.RentalResponseDTO;
import app.entity.Brand;
import app.entity.Car;
import app.entity.Landlords;
import app.entity.Rental;
import app.repository.CarRepository;
import app.repository.LandLordsRepository;
import app.repository.RentalRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RentalServiceTest {

    @Mock
    private RentalRepository rentalRepository;
    @Mock
    private CarRepository carRepository;
    @Mock
    private LandLordsRepository landlordsRepository;

    @InjectMocks
    private RentalService rentalService;

    private Car car;
    private Landlords landlord;
    private RentalDTO rentalDTO;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        car = new Car();
        car.setId(1L);
        car.setVehicleValue(50000.0);

        landlord = new Landlords();
        landlord.setId(1L);

        new RentalRequestDTO(
                car,
                landlord,
                LocalDateTime.of(2024, 1, 1, 10, 0),
                LocalDateTime.of(2024, 1, 10, 10, 0)
        );
    }

    @Test
    @DisplayName("Deve encontrar o aluguel com o id correspondente")
    void findByIdCase1() {
        Rental rental = new Rental();
        rental.setId(1L);

        when(rentalRepository.findById(1L)).thenReturn(Optional.of(rental));

        Rental result = rentalService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(rentalRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar um ID de um aluguel que não existe")
    void findByIdCase2() {
        when(rentalRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> rentalService.findById(99L));
    }

    @Test
    @DisplayName("Deve retornar uma lista com todos os alugueis do carro correspondente")
    void findByCarIdCase1() {
        Long carId = 1L;

        Brand brand = new Brand();
        brand.setId(1L);
        brand.setName("Ford");

        Car car = new Car();
        car.setId(carId);
        car.setName("Fiesta");
        car.setBrand(brand);
        car.setVehicleValue(100.00);

        Landlords landlord = new Landlords();
        landlord.setId(1L);
        landlord.setName("Eduardo Costa");

        Rental rentalEntity = new Rental();
        rentalEntity.setId(1L);
        rentalEntity.setCar(car);
        rentalEntity.setLandlord(landlord);
        rentalEntity.setStatus("ATIVO");

        when(carRepository.existsById(carId)).thenReturn(true);
        when(rentalRepository.findByCar_Id(carId)).thenReturn(List.of(rentalEntity));

        List<RentalResponseDTO> result = rentalService.findByCarId(carId);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).status()).isEqualTo("ATIVO");
        assertThat(result.get(0).car().name()).isEqualTo("Fiesta");

        verify(carRepository, times(1)).existsById(carId);
        verify(rentalRepository, times(1)).findByCar_Id(carId);
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar um ID de um carro que não existe")
    void findByCarIdCase2(){
        when(carRepository.existsById(99L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> rentalService.findByCarId(99L));
    }

    @Test
    @DisplayName("Deve retornar uma lista com todos os alugueis do locador correspondente")
    void findByLandlordIdCase1() {
        Long landlordId = 1L;

        Brand brand = new Brand();
        brand.setName("Ford");
        Car car = new Car();
        car.setName("Fiesta");
        car.setBrand(brand);
        car.setVehicleValue(100.0);

        Landlords landlord = new Landlords();
        landlord.setId(landlordId);
        landlord.setName("Sergio Ramos");

        Rental rentalEntity = new Rental();
        rentalEntity.setId(1L);
        rentalEntity.setLandlord(landlord);
        rentalEntity.setCar(car);
        rentalEntity.setStatus("ATIVO");

        when(landlordsRepository.existsById(landlordId)).thenReturn(true);
        when(rentalRepository.findByLandlord_Id(landlordId)).thenReturn(List.of(rentalEntity));

        List<RentalResponseDTO> result = rentalService.findByLandlordId(landlordId);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).status()).isEqualTo("ATIVO");
        assertThat(result.get(0).landlord().id()).isEqualTo(landlordId);
        assertThat(result.get(0).landlord().name()).isEqualTo("Sergio Ramos");

        verify(landlordsRepository, times(1)).existsById(landlordId);
        verify(rentalRepository, times(1)).findByLandlord_Id(landlordId);
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar um ID de locador que não existe")
    void findByLandlordIdCase2(){
        when(landlordsRepository.existsById(99L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> rentalService.findByLandlordId(99L));
    }

    @Test
    @DisplayName("Cria um aluguel com sucesso e todas as suas dependencias")
    void createCase1() {
        Car car = new Car();
        car.setId(1L);
        car.setVehicleValue(50000.00);

        Landlords landlord = new Landlords();
        landlord.setId(1L);

        RentalRequestDTO rentalRequestDTO = new RentalRequestDTO(car, landlord, LocalDateTime.now(), LocalDateTime.now().plusDays(2));

        when(carRepository.findById(1L)).thenReturn(Optional.of(car));
        when(landlordsRepository.findById(1L)).thenReturn(Optional.of(landlord));

        when(rentalRepository.findOverlappingRentals(anyLong(), any(), any())).thenReturn(Collections.emptyList());
        when(rentalRepository.save(any(Rental.class))).thenAnswer(invocation -> {
            Rental rentalSalvo = invocation.getArgument(0);
            rentalSalvo.setId(1L);
            return rentalSalvo;
        });

        Rental result = rentalService.create(rentalRequestDTO);

        assertNotNull(result);
        assertEquals("ATIVO", result.getStatus());
        assertTrue(result.getTotalValue() > 0);
        verify(rentalRepository, times(1)).save(any(Rental.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar criar um rental com um carro null")
    void createCase2() {
        Landlords landlords = new Landlords();
        landlords.setId(1L);
        landlords.setName("Gabriel Jesus");

        LocalDateTime startDate = LocalDateTime.of(2025, 9, 17, 9, 0);
        LocalDateTime endDate = LocalDateTime.of(2025, 9, 20, 12, 0);

        RentalRequestDTO rentalRequestDTO = new RentalRequestDTO(null, landlords, startDate, endDate);

        assertThrows(IllegalArgumentException.class, () -> rentalService.create(rentalRequestDTO));

        verifyNoInteractions(carRepository);
        verifyNoInteractions(rentalRepository);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar criar um rental com um landlord null")
    void createCase3(){
        Car car = new Car();
        car.setId(1L);
        car.setName("Fiesta");

        LocalDateTime startDate = LocalDateTime.of(2025, 9, 17, 9, 0);
        LocalDateTime endDate = LocalDateTime.of(2025, 9, 20, 12, 0);

        RentalRequestDTO rentalRequestDTO = new RentalRequestDTO(car, null, startDate, endDate);

        assertThrows(IllegalArgumentException.class, () -> rentalService.create(rentalRequestDTO));

        verifyNoInteractions(landlordsRepository);
        verifyNoInteractions(rentalRepository);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar criar um rental com um carro que não foi encontrado")
    void createCase4(){
        Car car = new Car();
        car.setId(99L);

        Landlords landlords = new Landlords();
        landlords.setId(1L);

        LocalDateTime startDate = LocalDateTime.of(2025, 9, 17, 9, 0);
        LocalDateTime endDate = LocalDateTime.of(2025, 9, 20, 12, 0);

        RentalRequestDTO rentalRequestDTO = new RentalRequestDTO(car, landlords, startDate, endDate);

        when(carRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> rentalService.create(rentalRequestDTO));
        verify(rentalRepository, never()).save(any(Rental.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar criar um rental com um landlord que não foi encontrado")
    void createCase5(){
        Car car = new Car();
        car.setId(1L);

        Landlords landlords = new Landlords();
        landlords.setId(99L);

        LocalDateTime startDate = LocalDateTime.of(2025, 9, 17, 9, 0);
        LocalDateTime endDate = LocalDateTime.of(2025, 9, 20, 12, 0);

        RentalRequestDTO rentalRequestDTO = new RentalRequestDTO(car, landlords, startDate, endDate);

        when(carRepository.findById(1L)).thenReturn(Optional.of(car)); 
        when(landlordsRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> rentalService.create(rentalRequestDTO));
        verify(rentalRepository, never()).save(any(Rental.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar criar um rental para um carro em uma data indisponível")
    void createCase6(){
        Car car = new Car();
        car.setId(1L);

        Landlords landlords = new Landlords();
        landlords.setId(1L);

        LocalDateTime startDate = LocalDateTime.of(2025, 9, 17, 9, 0);
        LocalDateTime endDate = LocalDateTime.of(2025, 9, 20, 12, 0);

        RentalRequestDTO rentalRequestDTO = new RentalRequestDTO(car, landlords, startDate, endDate);

        when(carRepository.findById(1L)).thenReturn(Optional.of(car));
        when(landlordsRepository.findById(1L)).thenReturn(Optional.of(landlords));

        Rental existingRental = new Rental();
        existingRental.setId(10L);
        existingRental.setCar(car);
        existingRental.setLandlord(landlords);
        existingRental.setStartDate(startDate);
        existingRental.setReturnDate(endDate);
        existingRental.setStatus("ATIVO");

        when(rentalRepository.findOverlappingRentals(car.getId(), startDate, endDate))
                .thenReturn(List.of(existingRental));

        assertThrows(IllegalStateException.class, () -> rentalService.create(rentalRequestDTO));

        verify(rentalRepository, never()).save(any(Rental.class));
    }

    @Test
    @DisplayName("Não deve atualizar nada quando não houver aluguéis ativos")
    void updateExpiredRentalsStatusCase1() {
        when(rentalRepository.findByStatus("ATIVO")).thenReturn(List.of());

        rentalService.updateExpiredRentalsStatus();

        verify(rentalRepository, never()).save(any(Rental.class));
    }

    @Test
    @DisplayName("Não deve atualizar quando os aluguéis ainda não expiraram")
    void updateExpiredRentalsStatusCase2() {
        Rental rental = new Rental();
        rental.setId(1L);
        rental.setStatus("ATIVO");
        rental.setReturnDate(LocalDateTime.now().plusDays(1));

        when(rentalRepository.findByStatus("ATIVO")).thenReturn(List.of(rental));

        rentalService.updateExpiredRentalsStatus();

        assertThat(rental.getStatus()).isEqualTo("ATIVO");
        verify(rentalRepository, never()).save(any(Rental.class));
    }

    @Test
    @DisplayName("Deve atualizar status para FINALIZADO quando o aluguel estiver expirado")
    void updateExpiredRentalsStatusCase3() {
        Rental rental = new Rental();
        rental.setId(1L);
        rental.setStatus("ATIVO");
        rental.setReturnDate(LocalDateTime.now().minusDays(1));

        when(rentalRepository.findByStatus("ATIVO")).thenReturn(List.of(rental));

        rentalService.updateExpiredRentalsStatus();

        assertThat(rental.getStatus()).isEqualTo("FINALIZADO");
        verify(rentalRepository, times(1)).save(rental);
    }
}
