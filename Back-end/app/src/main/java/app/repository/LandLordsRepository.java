package app.repository;

import app.entity.Landlords;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LandLordsRepository extends JpaRepository<Landlords, Long> {
    public Optional<Landlords> findByCpf(String cpf);
}
