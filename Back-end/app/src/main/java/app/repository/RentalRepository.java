package app.repository;

import app.entity.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface RentalRepository extends JpaRepository<Rental, Long> {

    @Query("SELECT r FROM Rental r WHERE r.car.id = :carId AND r.status = 'ATIVO' AND r.startDate < :endDate AND r.returnDate > :startDate")
    List<Rental> findOverlappingRentals(@Param("carId") Long carId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    List<Rental> findByLandlord_Id(Long landlordId);

    List<Rental> findByCar_Id(Long carId);

    List<Rental> findByStatus(String status);
}