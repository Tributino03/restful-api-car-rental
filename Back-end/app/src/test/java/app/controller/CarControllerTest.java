package app.controller;

import static org.junit.jupiter.api.Assertions.*;

import app.dto.BrandRequestDTO;
import app.dto.CarRequestDTO;
import app.entity.Brand;
import app.entity.Car;
import app.service.BrandService;
import app.service.CarService;
import app.service.FipeApiService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CarControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CarService carService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("Deve retornar 200 e a lista de carros")
    @WithMockUser
    void findAllCase1() throws Exception {
        mockMvc.perform(get("/api/car/findAll")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DisplayName("Deve retornar 500 quando o servico falha")
    @WithMockUser
    void findAllCase2() throws Exception {
        when(carService.findAll()).thenThrow(new RuntimeException("Erro de simulação"));

        mockMvc.perform(get("/api/car/findAll")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("Deve retornar 200 e o carro com o id correspondente")
    @WithMockUser
    void findByIdCase1() throws Exception {
        Brand brand = new Brand();
        brand.setId(1L);
        brand.setName("Ford");
        Car carRetorrno = new Car();
        carRetorrno.setId(1L);

        when(carService.findById(1L)).thenReturn(carRetorrno);

        mockMvc.perform(get("/api/car/findById/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

    }

    @Test
    @DisplayName("Deve retornar 404 quando o carro nao for encontrado")
    @WithMockUser
    void findByIdCase2() throws Exception {
        when(carService.findById(99L)).thenThrow(new EntityNotFoundException("Carro nao encontrado"));

        mockMvc.perform(get("/api/car/findById/{id}",99L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve retornar 200 e uma lista de carros")
    @WithMockUser
    void findByNameCase1() throws Exception {
        Brand brand = new Brand();
        brand.setId(1L);
        brand.setName("Ford");
        Car carRetorrno = new Car();
        carRetorrno.setId(1L);
        carRetorrno.setName("Fiesta");
        List<Car> carList = List.of(carRetorrno);

        when(carService.findByName("Fiesta")).thenReturn(carList);

        mockMvc.perform(get("/api/car/findByName")
                        .param("name", "Fiesta")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value("Fiesta"));
    }

    @Test
    @DisplayName("Deve retornar 200 e uma lista de carros vazia quando um carro nao é encontrada")
    @WithMockUser
    void findByNameCase2() throws Exception {
        when(carService.findByName("onix")).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/api/car/findByName")
                        .param("name","onix")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @DisplayName("Deve retornar 200 e uma lista de carros da marca correspondente")
    @WithMockUser
    void findByBrandCase1() throws Exception {
        Brand brand = new Brand();
        brand.setId(1L);
        brand.setName("Ford");

        Car car = new Car();
        car.setId(1L);
        car.setBrand(brand);
        car.setName("Fiesta");

        List <Car> carList = List.of(car);

        when(carService.findByBrand(1L)).thenReturn(carList);

        mockMvc.perform(get("/api/car/findByBrand")
                        .param("idBrand", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value("Fiesta"));
    }

    @Test
    @DisplayName("Deve retornar 200 e uma lista de carros vazia quando uma marca nao é encontrada")
    @WithMockUser
    void findByBrandCase2() throws Exception {
        when(carService.findByBrand(99L)).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/api/car/findByBrand")
                .param("idBrand", "99")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @DisplayName("Deve retornar 200 e uma lista de carros com o ano maior do que o ano que foi passado")
    @WithMockUser
    void findAboveYearCase1() throws Exception {
        Brand brand = new Brand();
        brand.setId(1L);
        brand.setName("Ford");

        Car car = new Car();
        car.setId(1L);
        car.setBrand(brand);
        car.setName("Fiesta");
        car.setModelYear(2015);

        List <Car> carList = List.of(car);

        when(carService.findAboveYear(2014)).thenReturn(carList);

        mockMvc.perform(get("/api/car/findAboveYear")
                .param("modelYear", "2014")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value("Fiesta"));
    }

    @Test
    @DisplayName("Deve retornar 200 e uma lista de carros vazia quando nao existe carros com o ano maior do que o que foi passado")
    @WithMockUser
    void findAboveYearCase2() throws Exception {
        when(carService.findAboveYear(2025)).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/api/car/findAboveYear")
                .param("modelYear","2025")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

    }

    @Test
    @DisplayName("create deve retornar 201 e o novo carro")
    @WithMockUser(roles = "ADMIN")
    void createCase1() throws Exception {
        Brand brandSalva = new Brand();
        brandSalva.setId(10L);
        brandSalva.setName("Ford");

        CarRequestDTO carRequestDTO = new CarRequestDTO("Fiesta", 2015, 25750.00, brandSalva);
        Car car = new Car();
        car.setId(1L);
        car.setName("Fiesta");
        car.setBrand(brandSalva);

        when(carService.create(any(CarRequestDTO.class))).thenReturn(car);

        mockMvc.perform(post("/api/car/create")
                .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(carRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Fiesta"));

    }

    @Test
    @DisplayName("create deve retornar 400 pois nao pode criar um carro com a marrca null")
    @WithMockUser(roles = "ADMIN")
    void createCase2() throws Exception {

        CarRequestDTO carRequestDTO = new CarRequestDTO("Fiesta", 2015, 25750.00, null);
        Car car = new Car();
        car.setId(1L);
        car.setName("Fiesta");
        car.setBrand(null);

        when(carService.create(any(CarRequestDTO.class))).thenThrow(new IllegalArgumentException("A marca do carro precisa ser selecionada nao pode ser null"));

        mockMvc.perform(post("/api/car/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(carRequestDTO)))
                .andExpect(status().isBadRequest());

    }

    @Test
    @DisplayName("create deve retornar 404 pois nao encontrou a marca que foi associada ao carro")
    @WithMockUser(roles = "ADMIN")
    void createCase3() throws Exception {
        Brand brandSalva = new Brand();
        brandSalva.setName("Ford");

        CarRequestDTO carRequestDTO = new CarRequestDTO("Fiesta", 2015, 25750.00, brandSalva);
        Car car = new Car();
        car.setId(1L);
        car.setName("Fiesta");
        car.setBrand(brandSalva);

        when(carService.create(any(CarRequestDTO.class))).thenThrow(new EntityNotFoundException("Marca nao encontrada"));

        mockMvc.perform(post("/api/car/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(carRequestDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("update deve retornar 200 e o carro atualizado")
    @WithMockUser(roles = "ADMIN")
    void updateCase1() throws Exception {
        Long carId = 1L;
        Brand brand = new Brand();
        brand.setId(1L);
        CarRequestDTO requestDTO = new CarRequestDTO("Fiesta Atualizado", 2021, 55000.0, brand);

        Car carAtualizado = new Car();
        carAtualizado.setId(carId);
        carAtualizado.setName("Fiesta Atualizado");
        carAtualizado.setModelYear(2021);

        when(carService.update(eq(carId), any(CarRequestDTO.class))).thenReturn(carAtualizado);

        mockMvc.perform(put("/api/car/update/{id}", carId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(carId))
                .andExpect(jsonPath("$.name").value("Fiesta Atualizado"));
    }

    @Test
    @DisplayName("update deve retornar 400 para uma marca nula")
    @WithMockUser(roles = "ADMIN")
    void updateCase2() throws Exception {
        Long carId = 1L;
        CarRequestDTO requestDTO = new CarRequestDTO("Fiesta", 2020, 50000.0, null); // Marca nula

        when(carService.update(eq(carId), any(CarRequestDTO.class)))
                .thenThrow(new IllegalArgumentException("A marca do carro precisa ser selecionada."));

        mockMvc.perform(put("/api/car/update/{id}", carId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("update deve retornar 404 para uma marca não encontrada")
    @WithMockUser(roles = "ADMIN")
    void updateCase3() throws Exception {
        Long carId = 1L;
        Brand brand = new Brand();
        brand.setId(99L);
        CarRequestDTO requestDTO = new CarRequestDTO("Fiesta", 2020, 50000.0, brand);

        when(carService.update(eq(carId), any(CarRequestDTO.class)))
                .thenThrow(new EntityNotFoundException("A marca com o ID 99 não foi encontrada."));

        mockMvc.perform(put("/api/car/update/{id}", carId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve retornar 204 para exclusão com sucesso")
    @WithMockUser(roles = "ADMIN")
    void deleteCase1() throws Exception {
        mockMvc.perform(delete("/api/car/delete/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Deve retornar 409 para conflito (carros com alugueis)")
    @WithMockUser(roles = "ADMIN")
    void deleteCase2() throws Exception {
        doThrow(new IllegalStateException("Este carro nao pode ser excluido pois a alugueis associados a ele"))
                .when(carService).delete(1L);

        mockMvc.perform(delete("/api/car/delete/1"))
                .andExpect(status().isConflict()); 
    }

}