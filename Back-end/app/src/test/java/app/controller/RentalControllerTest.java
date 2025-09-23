package app.controller;

import app.dto.CarSimpleDTO;
import app.dto.LandlordSimpleDTO;
import app.dto.RentalRequestDTO;
import app.dto.RentalResponseDTO;
import app.entity.Car;
import app.entity.Landlords;
import app.entity.Rental;
import app.service.RentalService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class RentalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RentalService rentalService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("findAll deve retornar 200 e a lista de aluguéis")
    @WithMockUser
    void findAllCase1() throws Exception {
        CarSimpleDTO carDTO = new CarSimpleDTO(1L, "Fiesta");
        LandlordSimpleDTO landlordDTO = new LandlordSimpleDTO(1L, "José");
        RentalResponseDTO rentalResponse = new RentalResponseDTO(1L, LocalDateTime.now(), LocalDateTime.now().plusDays(1), 200.0, "ATIVO", carDTO, landlordDTO);

        when(rentalService.findAll()).thenReturn(List.of(rentalResponse));

        mockMvc.perform(get("/api/rental/findAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    @DisplayName("findById deve retornar 200 e o aluguel encontrado")
    @WithMockUser
    void findByIdCase1() throws Exception {
        Rental rental = new Rental();
        rental.setId(1L);
        rental.setStatus("ATIVO");
        when(rentalService.findById(1L)).thenReturn(rental);

        mockMvc.perform(get("/api/rental/findById/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("ATIVO"));
    }

    @Test
    @DisplayName("findById deve retornar 404 para ID não encontrado")
    @WithMockUser
    void findByIdCase2() throws Exception {
        when(rentalService.findById(99L)).thenThrow(new EntityNotFoundException("Aluguel não encontrado"));

        mockMvc.perform(get("/api/rental/findById/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("findByCarId deve retornar 200 e a lista de aluguéis do carro")
    @WithMockUser
    void findByCarIdCase1() throws Exception {
        when(rentalService.findByCarId(1L)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/rental/car/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DisplayName("findByLandlordId deve retornar 200 e a lista de aluguéis do locador")
    @WithMockUser
    void findByLandlordIdCase1() throws Exception {
        when(rentalService.findByLandlordId(1L)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/rental/landlord/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DisplayName("create deve retornar 201 e o novo aluguel")
    @WithMockUser(roles = "ADMIN")
    void createCase1() throws Exception {
        Car car = new Car();
        car.setId(1L);
        Landlords landlord = new Landlords();
        landlord.setId(1L);
        RentalRequestDTO requestDTO = new RentalRequestDTO(car, landlord, LocalDateTime.now(), LocalDateTime.now().plusDays(2));

        Rental rentalSalvo = new Rental();
        rentalSalvo.setId(10L);

        when(rentalService.create(any(RentalRequestDTO.class))).thenReturn(rentalSalvo);

        mockMvc.perform(post("/api/rental/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10L));
    }

    @Test
    @DisplayName("create deve retornar 409 para conflito de datas")
    @WithMockUser(roles = "ADMIN")
    void createCase2_WhenDatesOverlap_ShouldReturnConflict() throws Exception {
        Car car = new Car();
        car.setId(1L);
        Landlords landlord = new Landlords();
        landlord.setId(1L);
        RentalRequestDTO requestDTO = new RentalRequestDTO(car, landlord, LocalDateTime.now(), LocalDateTime.now().plusDays(2));

        when(rentalService.create(any(RentalRequestDTO.class)))
                .thenThrow(new IllegalStateException("Este carro já está alugado para o período solicitado."));

        mockMvc.perform(post("/api/rental/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isConflict());
    }
}