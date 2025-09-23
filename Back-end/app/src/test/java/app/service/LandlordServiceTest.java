package app.service;

import app.dto.LandlordRequestDTO;
import app.entity.Address;
import app.entity.Landlords;
import app.repository.LandLordsRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LandlordServiceTest {

    @Mock
    private LandLordsRepository landlordsRepository;

    @InjectMocks
    private LandlordService landlordService;

    @Captor
    private ArgumentCaptor<Landlords> landlordCaptor;

    @Test
    @DisplayName("Deve encontrar o locador com o id correspondente")
    void findByIdCase1(){
        Landlords landlords = new Landlords();
        landlords.setId(1L);

        when(landlordsRepository.findById(1L)).thenReturn(Optional.of(landlords));

        Landlords result = landlordService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(landlordsRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve lancar uma execao pois o id passado nao existe")
    void findByIdCase2(){
        when(landlordsRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> landlordService.findById(99L));
    }

    @Test
    @DisplayName("Deve encontrar o locador com o id correspondente")
    void findByCpfCase1(){
        Landlords landlords = new Landlords();
        landlords.setCpf("379.291.110-83");

        when(landlordsRepository.findByCpf("379.291.110-83")).thenReturn(Optional.of(landlords));

        Landlords result = landlordService.findByCpf("379.291.110-83");

        assertNotNull(result);
        assertEquals("379.291.110-83", result.getCpf());
        verify(landlordsRepository, times(1)).findByCpf("379.291.110-83");
    }

    @Test
    @DisplayName("Deve lancar uma execao pois o id passado nao existe")
    void findByCpfCase2(){
        when(landlordsRepository.findByCpf("343.291.110-00")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> landlordService.findByCpf("343.291.110-00"));
    }

    @Test
    @DisplayName("Deve criar um locador com sucesso")
    void createCase1() {

        Address address = new Address();
        address.setCep("12345-678");
        LandlordRequestDTO landlordRequestDTO = new LandlordRequestDTO(
                "Cristiano Ronaldo",
                LocalDate.of(1985, 2, 5),
                "071.878.700-51",
                address
        );

        when(landlordsRepository.findByCpf("071.878.700-51")).thenReturn(Optional.empty());

        when(landlordsRepository.save(any(Landlords.class))).thenAnswer(invocation -> {
            Landlords landlordSalvo = invocation.getArgument(0);
            landlordSalvo.setId(1L);
            return landlordSalvo;
        });


        Landlords result = landlordService.create(landlordRequestDTO);

        assertNotNull(result);
        assertEquals("Cristiano Ronaldo", result.getName());
        assertNotNull(result.getId());
        verify(landlordsRepository, times(1)).save(any(Landlords.class));
    }

    @Test
    @DisplayName("Deve lancar uma execao ao tentar criar um locador com um cpf que já existe no banco de dados")
    void createCase2(){
        Address address = new Address();
        address.setCep("12345-678");
        LandlordRequestDTO landlordRequestDTO = new LandlordRequestDTO(
                "Lionel Messi",
                LocalDate.of(1985, 2, 5),
                "087.543.745-67",
                address
        );

        when(landlordsRepository.findByCpf("087.543.745-67")).thenReturn(Optional.of(new Landlords()));

        assertThrows(IllegalArgumentException.class, () -> landlordService.create(landlordRequestDTO));
        verify(landlordsRepository, never()).save(any(Landlords.class));
    }

    @Test
    @DisplayName("Deve atualizar apenas o endereço de um locador com sucesso")
    void updateAddressCase1() {
        Long landlordId = 1L;

        Address oldAddress = new Address();
        oldAddress.setCep("00000-000");
        Landlords existingLandlord = new Landlords();
        existingLandlord.setId(landlordId);
        existingLandlord.setName("Nome Antigo");
        existingLandlord.setAddress(oldAddress);

        Address newAddressDetails = new Address();
        newAddressDetails.setCep("99999-999");
        newAddressDetails.setStreet("Rua Nova");

        when(landlordsRepository.findById(landlordId)).thenReturn(Optional.of(existingLandlord));
        when(landlordsRepository.save(any(Landlords.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Landlords result = landlordService.updateAddress(landlordId, newAddressDetails);

        verify(landlordsRepository).save(landlordCaptor.capture());
        Landlords savedLandlord = landlordCaptor.getValue();

        assertEquals("99999-999", savedLandlord.getAddress().getCep());
        assertEquals("Rua Nova", savedLandlord.getAddress().getStreet());
        assertEquals("Nome Antigo", savedLandlord.getName());
    }

}