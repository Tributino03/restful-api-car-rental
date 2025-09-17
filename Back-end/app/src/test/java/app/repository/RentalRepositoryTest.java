package app.repository;

import app.entity.*;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class RentalRepositoryTest {

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private EntityManager entityManager;

    private Car carForTest;
    private Car carForTest2;
    private Landlords landlordForTest;
    private Landlords landlordForTest2;

    @BeforeEach
    void setUp() {
        Brand ford = new Brand();
        ford.setName("Ford");
        entityManager.persist(ford);

        Address address = new Address();
        address.setCep("12345-678");
        entityManager.persist(address);

        this.carForTest = new Car();
        carForTest.setName("Fiesta");
        carForTest.setModelYear(2014);
        carForTest.setBrand(ford);
        entityManager.persist(carForTest);

        this.carForTest2 = new Car();
        carForTest2.setName("EcoSport");
        carForTest2.setModelYear(2020);
        carForTest2.setBrand(ford);
        entityManager.persist(carForTest2);

        this.landlordForTest = new Landlords();
        landlordForTest.setName("Pedro Silva");
        landlordForTest.setCpf("111.222.333-44");
        landlordForTest.setDateOfBirth(LocalDate.of(1999, 9, 9));
        landlordForTest.setAddress(address);
        entityManager.persist(landlordForTest);

        this.landlordForTest2 = new Landlords();
        landlordForTest2.setName("Gabriel Santos");
        landlordForTest2.setCpf("377.874.480-16");
        landlordForTest2.setDateOfBirth(LocalDate.of(1999, 9, 9));

        Address address2 = new Address();
        address2.setCep("87654-321");
        entityManager.persist(address2);
        landlordForTest2.setAddress(address2);
        entityManager.persist(landlordForTest2);

        Rental rental = new Rental();
        rental.setCar(carForTest);
        rental.setLandlord(landlordForTest);
        rental.setStartDate(LocalDateTime.of(2025, 9, 16, 10, 0));
        rental.setReturnDate(LocalDateTime.of(2025, 9, 19, 10, 0));
        rental.setStatus("ATIVO");
        entityManager.persist(rental);
    }

    @Test
    @DisplayName("Deve retornar uma lista com os alugueis conflitantes quando as datas se sobrepõem")
    void findOverlappingRentalsCase1(){
        LocalDateTime startDate = LocalDateTime.of(2025, 9, 17, 10, 0);
        LocalDateTime endDate = LocalDateTime.of(2025, 9, 18, 10, 0);
        List<Rental> result = rentalRepository.findOverlappingRentals(carForTest.getId(), startDate, endDate);

        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("Deve retornar uma lista vazia quando os alugueis nao se sobrepõem")
    void findOverlappingRentalsCase2(){
        LocalDateTime startDate = LocalDateTime.of(2025, 9, 20, 10, 0);
        LocalDateTime endDate = LocalDateTime.of(2025, 9, 22, 10, 0);
        List<Rental> result = rentalRepository.findOverlappingRentals(carForTest.getId(), startDate, endDate);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Deve retorna uma lista com os alugueis correpondentes ao locador")
    void findByLandlord_IdCase1(){
        List<Rental> result = rentalRepository.findByLandlord_Id(landlordForTest.getId());

        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("Deve retorna uma lista vazia pois esse locador nao tem nenhum aluguel")
    void findByLandlord_IdCase2(){
        List<Rental> result = rentalRepository.findByLandlord_Id(landlordForTest2.getId());

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Deve retornar uma lista com os alugueis correpondentes ao carro")
    void findByCar_IdCase1(){
        List<Rental> result = rentalRepository.findByCar_Id(carForTest.getId());

        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("Deve retorna uma lista vazia pois esse carro nao tem nenhum aluguel")
    void findByCar_IdCase2(){
        List<Rental> result = rentalRepository.findByCar_Id(carForTest2.getId());

        assertThat(result).isEmpty();
    }


    @Test
    @DisplayName("Deve retornar uma lista com aluguéis de um status específico")
    void findByStatusCase1() {
        List<Rental> result = rentalRepository.findByStatus("ATIVO");

        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("Deve retornar uma lista vazia para um status que não existe em nenhum aluguel")
    void findByStatus_WhenStatusDoesNotExist_ShouldReturnEmptyList() {
        List<Rental> result = rentalRepository.findByStatus("FINALIZADO");
        
        assertThat(result).isEmpty();
    }

}