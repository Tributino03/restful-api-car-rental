package app.service;

import app.entity.Address;
import app.entity.Landlords;
import app.repository.LandLordsRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LandlordService {

    @Autowired
    private LandLordsRepository landLordsRepository;

    public Landlords findById(Long id) {
        return landLordsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Locador não encontrado com o id: " + id));
    }

    public Landlords findByCpf(String cpf) {
        return landLordsRepository.findByCpf(cpf)
                .orElseThrow(() -> new EntityNotFoundException("Nenhum locador encontrado com o CPF: " + cpf));
    }

    public List<Landlords> findAll() {
        return landLordsRepository.findAll();
    }

    public Landlords create(Landlords landlord) {
        if (landLordsRepository.findByCpf(landlord.getCpf()).isPresent()) {
            throw new IllegalArgumentException("Já existe um locador cadastrado com este CPF.");
        }
        return landLordsRepository.save(landlord);
    }

    public Landlords updateAddress(Long landlordId, Address newAddressDetails) {
        Landlords landlordFromDb = this.findById(landlordId);

        if (landlordFromDb.getAddress() == null) {
            landlordFromDb.setAddress(new Address());
        }

        landlordFromDb.getAddress().setCep(newAddressDetails.getCep());
        landlordFromDb.getAddress().setStreet(newAddressDetails.getStreet());
        landlordFromDb.getAddress().setNumber(newAddressDetails.getNumber());
        landlordFromDb.getAddress().setNeighborhood(newAddressDetails.getNeighborhood());
        landlordFromDb.getAddress().setCity(newAddressDetails.getCity());
        landlordFromDb.getAddress().setState(newAddressDetails.getState());

        return landLordsRepository.save(landlordFromDb);
    }

}