package app.repository;

import app.entity.Car;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class CarRepositoryTest {

    @Autowired
    CarRepository carRepository;

    @Test
    @DisplayName("retorna a lista de carro ordenada por ano com sucesso")
    void findAboveYearCase1(){

    }
}