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

    private Brand ford;

    private Brand chevrolet;

    private Brand fiat;

    @BeforeEach
    void setUp() {
        this.ford = new Brand();
        this.ford.setName("Ford");
        this.ford.setFipeCode(22L);
        entityManager.persist(this.ford);

        this.chevrolet = new Brand();
        this.chevrolet.setName("Chevrolet");
        this.chevrolet.setFipeCode(23L);
        entityManager.persist(this.chevrolet);

        this.fiat = new Brand();
        this.fiat.setName("Fiat");
        this.fiat.setFipeCode(21L);
        entityManager.persist(this.fiat);

        Car car1 = new Car();
        car1.setName("Fiesta");
        car1.setModelYear(2014);
        car1.setBrand(this.ford);
        entityManager.persist(car1);

        Car car2 = new Car();
        car2.setName("Onix");
        car2.setModelYear(2018);
        car2.setBrand(this.chevrolet);
        entityManager.persist(car2);

        Car car3 = new Car();
        car3.setName("EcoSport");
        car3.setModelYear(2020);
        car3.setBrand(this.ford);
        entityManager.persist(car3);

        Car car4 = new Car();
        car4.setName("Fiesta");
        car4.setModelYear(2013);
        car4.setBrand(this.ford);
        entityManager.persist(car4);
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

    @Test
    @DisplayName("Deve retornar uma lista de carros com o nome correspondente")
    void findByNameCased01(){
        List<Car> result = carRepository.findByName("Fiesta");

        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("Deve retornar uma lista de carros vazia")
    void findByNameCased02(){
        List<Car> result = carRepository.findByName("IX35");

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Deve retornar uma lista de carros correspondentes a marca")
    void findByBrandCase01(){
        List<Car> result = carRepository.findByBrand(ford);

        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(3);
    }

    @Test
    @DisplayName("Deve retornar uma lista de carros vazia")
    void findByBrandCase02(){
        List<Car> result = carRepository.findByBrand(fiat);

        assertThat(result).isEmpty();
    }
}