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
    RentalRepository rentalRepository;

    @Autowired
    EntityManager entityManager;

    private Car carForTest;

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

        Landlords landlord = new Landlords();
        landlord.setName("Pedro Silva");
        landlord.setCpf("111.222.333-44");
        landlord.setDateOfBirth(LocalDate.of(1999, 9, 9));
        landlord.setAddress(address);
        entityManager.persist(landlord);

        Rental rental = new Rental();
        rental.setCar(carForTest);
        rental.setLandlord(landlord);
        rental.setStartDate(LocalDateTime.of(2025, 9, 16, 10, 0));
        rental.setReturnDate(LocalDateTime.of(2025, 9, 19, 10, 0));
        rental.setStatus("ATIVO");
        entityManager.persist(rental);

    }

    @Test
    @DisplayName("Deve encontrar um aluguel conflitante quando as datas se sobrepõem")
    void findOverlappingRentalsCase1(){
        LocalDateTime startDate = LocalDateTime.of(2025, 9, 17, 10, 0);
        LocalDateTime endDate = LocalDateTime.of(2025, 9, 18, 10, 0);
        List<Rental> result = rentalRepository.findOverlappingRentals(carForTest.getId(), startDate, endDate);

        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("Nao deve encontrar um aluguel conflitante pois as nao se sobrepõem")
    void findOverlappingRentalsCase2(){
        LocalDateTime startDate = LocalDateTime.of(2025, 9, 20, 10, 0);
        LocalDateTime endDate = LocalDateTime.of(2025, 9, 22, 10, 0);
        List<Rental> result = rentalRepository.findOverlappingRentals(carForTest.getId(), startDate, endDate);

        assertThat(result).isEmpty();
    }

}