package app.repository;

import app.entity.Brand;
import app.entity.Car;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CarRepositoryTest {

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void setUp() {
        Brand ford = new Brand();
        ford.setName("Ford");
        ford.setFipeCode(22L);
        entityManager.persist(ford);

        Brand chevrolet = new Brand();
        chevrolet.setName("Chevrolet");
        chevrolet.setFipeCode(23L);
        entityManager.persist(chevrolet);

        Car car1 = new Car();
        car1.setName("Fiesta");
        car1.setModelYear(2014);
        car1.setBrand(ford);
        entityManager.persist(car1);

        Car car2 = new Car();
        car2.setName("Onix");
        car2.setModelYear(2018);
        car2.setBrand(chevrolet);
        entityManager.persist(car2);

        Car car3 = new Car();
        car3.setName("EcoSport");
        car3.setModelYear(2020);
        car3.setBrand(ford);
        entityManager.persist(car3);
    }

    @Test
    @DisplayName("Deve retornar uma lista de carros com ano maior que o especificado")
    void findAboveYearCase1() {
        List<Car> result = carRepository.findAboveYear(2015);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Onix");
        assertThat(result.get(1).getName()).isEqualTo("EcoSport");
    }

    @Test
    @DisplayName("Deve retornar uma lista vazia quando o ano for maior que todos os existentes")
    void findAboveYearCase2() {
        List<Car> result = carRepository.findAboveYear(2022);

        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
    }
}