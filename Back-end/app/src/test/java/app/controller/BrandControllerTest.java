package app.controller;

import app.dto.BrandRequestDTO;
import app.entity.Brand;
import app.service.BrandService;
import app.service.FipeApiService;
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

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class BrandControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BrandService brandService;

    @MockBean
    private FipeApiService fipeApiService;

    @Autowired
    private ObjectMapper objectMapper;;

    @Test
    @DisplayName("Deve retornar OK (200) e a lista de brands")
    @WithMockUser
    void findAllCase1() throws Exception {
        mockMvc.perform(get("/api/brand/findAll")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DisplayName("Deve retornar um erro 500 quando o serviço falha")
    @WithMockUser
    void findAllCase2() throws Exception {
        when(brandService.findAll()).thenThrow(new RuntimeException("Erro de simulação"));

        mockMvc.perform(get("/api/brand/findAll"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("Deve retornar 200 e a marca com o id correspondente")
    @WithMockUser
    void findByIdCase1() throws Exception {
        Brand brandDeRetorno = new Brand();
        brandDeRetorno.setId(1L);
        when(brandService.findById(1L)).thenReturn(brandDeRetorno);

        mockMvc.perform(get("/api/brand/findById/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("Deve retornar 404 pois nao conseguiu encontrar a marca com id correspondente")
    @WithMockUser
    void findByIdCase2() throws Exception {
        when(brandService.findById(99L)).thenThrow(new EntityNotFoundException("Marca não encontrada"));

        mockMvc.perform(get("/api/brand/findById/{id}", 99L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve retornar 200 e a marca com o name correspondente")
    @WithMockUser
    void findByNameCase1() throws Exception {
        Brand brandRetorno = new Brand();
        brandRetorno.setId(1L);
        brandRetorno.setName("Ford");
        List<Brand> brandList = List.of(brandRetorno);

        when(brandService.findByName("Ford")).thenReturn(brandList);

        mockMvc.perform(get("/api/brand/findByName")
                        .param("name", "Ford")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value("Ford"));
    }

    @Test
    @DisplayName("Deve retornar 200 e uma lista vazia quando uma marca nao é encontrada")
    @WithMockUser
    void findByNameCase2() throws Exception {
        List<Brand> brandList = new ArrayList<>();
        when(brandService.findByName("xxxx")).thenReturn(brandList);

        mockMvc.perform(get("/api/brand/findByName")
                        .param("name", "xxxx")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @DisplayName("create deve retornar 201 e a nova marca")
    @WithMockUser(roles = "ADMIN")
    void createCase1() throws Exception {
        BrandRequestDTO requestDTO = new BrandRequestDTO("Ford", 123L, "11.222.333/0001-44");
        Brand brandSalva = new Brand();
        brandSalva.setId(10L);
        brandSalva.setName("Ford");

        when(brandService.create(any(BrandRequestDTO.class))).thenReturn(brandSalva);

        mockMvc.perform(post("/api/brand/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10L))
                .andExpect(jsonPath("$.name").value("Ford"));
    }

    @Test
    @DisplayName("create deve retornar 400 para uma marca que já existe no banco de dados")
    @WithMockUser(roles = "ADMIN")
    void createCase2() throws Exception {
        BrandRequestDTO requestDTO = new BrandRequestDTO("Ford", 123L, "11.222.333/0001-44");
        Brand brandSalva = new Brand();
        brandSalva.setId(10L);
        brandSalva.setName("Ford");

        when(brandService.create(any(BrandRequestDTO.class))).thenThrow(new IllegalArgumentException("Marca já cadastrada no banco de dados"));

        mockMvc.perform(post("/api/brand/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest());

    }

    @Test
    @DisplayName("delete deve retornar 204 para exclusão com sucesso")
    @WithMockUser(roles = "ADMIN")
    void deleteCase1() throws Exception {
        mockMvc.perform(delete("/api/brand/delete/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("delete deve retornar 409 para conflito (marca com carros)")
    @WithMockUser(roles = "ADMIN")
    void deleteCase2() throws Exception {
        doThrow(new IllegalStateException("Esta marca não pode ser excluída pois a carros associados a ela"))
                .when(brandService).delete(1L);

        mockMvc.perform(delete("/api/brand/delete/1"))
                .andExpect(status().isConflict());
    }

}
