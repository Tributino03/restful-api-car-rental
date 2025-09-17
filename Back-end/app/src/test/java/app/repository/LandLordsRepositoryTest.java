package app.repository;

import app.entity.Address;
import app.entity.Landlords;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class LandLordsRepositoryTest {

    @Autowired
    LandLordsRepository landLordsRepository;

    @Autowired
    TestEntityManager testEntityManager;

    @BeforeEach
    void setUp(){

        Address address = new Address();
        address.setCep("12345-678");
        testEntityManager.persist(address);

        Landlords landlords = new Landlords();
        landlords.setName("Ricardo Oliveira");
        landlords.setCpf("864.610.440-00");
        landlords.setDateOfBirth(LocalDate.of(2000, 8, 12));
        landlords.setAddress(address);
        testEntityManager.persist(landlords);
    }

    @Test
    @DisplayName("Deve retorna o locador com o cpf correspondente")
    void findByCpfCase1(){
        Optional<Landlords> result = 
    }

}