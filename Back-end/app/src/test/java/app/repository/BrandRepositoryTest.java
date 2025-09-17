package app.repository;

import app.entity.Brand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class BrandRepositoryTest {

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void setUp() {
        Brand ford = new Brand();
        ford.setName("Ford");
        ford.setFipeCode(22L);
        entityManager.persist(ford);
    }

    @Test
    @DisplayName("Deve retorna uma lista com a marca correspondente")
    void findByNameCase1(){
        List<Brand> result = brandRepository.findByName("Ford");

        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("Deve retorna uma lista vazia")
    void findByNameCase2(){
        List<Brand> result = brandRepository.findByName("Fiat");

        assertThat(result).isEmpty();
    }

}