package app.repository;

import app.entity.Landlords;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LandLordsRepository extends JpaRepository<Landlords, Long> {
}
