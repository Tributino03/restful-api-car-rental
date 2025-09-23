package app.controller;

import app.dto.LandlordRequestDTO;
import app.entity.Address;
import app.entity.Landlords;
import app.service.CarService;
import app.service.LandlordService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class LandlordControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    LandlordService landlordService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("Deve retornar 200 e o landlord com o id correspondente")
    @WithMockUser
    void findByIdCase01() throws Exception {
        Landlords landlord = new Landlords();
        landlord.setId(1L);

        when(landlordService.findById(1L)).thenReturn(landlord);

        mockMvc.perform(get("/api/landlord/findById/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"));
    }

    @Test
    @DisplayName("Deve retornar 404 se o landlord com o id correspondente nao for encontrado")
    @WithMockUser
    void findByIdCase02() throws Exception {

        when(landlordService.findById(99L)).thenThrow(new EntityNotFoundException("Locador não encontrado com o id"));

        mockMvc.perform(get("/api/landlord/findById/{id}", 99L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve retornar 200 e o landlord com o cpf correspondente")
    @WithMockUser
    void findByCpfCase01() throws Exception {
        Landlords landlord = new Landlords();
        landlord.setId(1L);
        landlord.setCpf("583.175.720-05");

        when(landlordService.findByCpf("583.175.720-05")).thenReturn(landlord);

        mockMvc.perform(get("/api/landlord/findByCpf")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("cpf", "583.175.720-05"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cpf").value("583.175.720-05"));
    }

    @Test
    @DisplayName("Deve retornar 404 se o landlord com o cpf correspondente nao for encontrado")
    @WithMockUser
    void findByCpfCase02() throws Exception {
        when(landlordService.findByCpf("151.705.100-20")).thenThrow(new EntityNotFoundException("Nenhum locador encontrado com o CPF"));

        mockMvc.perform(get("/api/landlord/findByCpf")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("cpf", "151.705.100-20"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve retornar 200 e uma lista de landlord")
    @WithMockUser
    void findAllCase01() throws Exception {
        Landlords landlord = new Landlords();
        landlord.setId(1L);
        landlord.setCpf("583.175.720-05");
        landlord.setName("Neymar JR");
        List<Landlords> landlordsList = List.of(landlord);

        when(landlordService.findAll()).thenReturn(landlordsList);

        mockMvc.perform(get("/api/landlord/findAll")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DisplayName("Deve retornar 500 quando acontece um erro no servidor")
    @WithMockUser
    void findAllCase02() throws Exception {
        when(landlordService.findAll()).thenThrow(new RuntimeException("Erro de simulação"));

        mockMvc.perform(get("/api/landlord/findAll")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("Deve retornar 201 e o novo locador")
    @WithMockUser(roles = "ADMIN")
    void createCase01() throws Exception {
        Address address = new Address();
        address.setCep("12345-678");
        LandlordRequestDTO landlordRequestDTO = new LandlordRequestDTO("Cristiano Ronaldo", LocalDate.of(1985, 2, 5), "583.175.720-05", address);
        Landlords landlord = new Landlords();
        landlord.setId(1L);
        landlord.setName("Cristiano Ronaldo");
        landlord.setCpf("583.175.720-05");

        when(landlordService.create(any(LandlordRequestDTO.class))).thenReturn(landlord);

        mockMvc.perform(post("/api/landlord/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(landlordRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("Deve retornar 404 e pois ja existe um locador com esse cpf")
    @WithMockUser(roles = "ADMIN")
    void createCase02() throws Exception {
        Address address = new Address();
        address.setCep("12345-678");
        LandlordRequestDTO landlordRequestDTO = new LandlordRequestDTO("Cristiano Ronaldo", LocalDate.of(1985, 2, 5), "583.175.720-05", address);

        when(landlordService.create(any(LandlordRequestDTO.class))).thenThrow(new IllegalArgumentException("Já existe um locador cadastrado com este CPF."));

        mockMvc.perform(post("/api/landlord/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(landlordRequestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("updateAddress deve retornar 200 e o locador com endereço atualizado")
    @WithMockUser(roles = "ADMIN")
    void updateAddressCase1() throws Exception {
        Long landlordId = 1L;

        Address newAddressDetails = new Address();
        newAddressDetails.setCep("99999-999");
        newAddressDetails.setStreet("Rua Nova");

        Landlords landlordAtualizado = new Landlords();
        landlordAtualizado.setId(landlordId);
        landlordAtualizado.setName("Nome Original");
        landlordAtualizado.setAddress(newAddressDetails);

        when(landlordService.updateAddress(eq(landlordId), any(Address.class))).thenReturn(landlordAtualizado);

        mockMvc.perform(put("/api/landlord/updateAddress/{id}", landlordId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newAddressDetails))) 
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(landlordId))
                .andExpect(jsonPath("$.address.cep").value("99999-999"))
                .andExpect(jsonPath("$.address.street").value("Rua Nova"));
    }

    @Test
    @DisplayName("updateAddress deve retornar 404 se o locador não for encontrado")
    @WithMockUser(roles = "ADMIN")
    void updateAddressCase2() throws Exception {
        Long landlordIdQueNaoExiste = 99L;
        Address newAddressDetails = new Address();
        newAddressDetails.setCep("99999-999");

        when(landlordService.updateAddress(eq(landlordIdQueNaoExiste), any(Address.class)))
                .thenThrow(new EntityNotFoundException("Locador não encontrado com o id: " + landlordIdQueNaoExiste));

        mockMvc.perform(put("/api/landlord/updateAddress/{id}", landlordIdQueNaoExiste)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newAddressDetails)))
                .andExpect(status().isNotFound());
    }
}