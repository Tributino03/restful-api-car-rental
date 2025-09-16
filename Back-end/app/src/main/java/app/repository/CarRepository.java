package app.repository;

import app.entity.Brand;
import app.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

    public List<Car> findByName(String name);

    public List<Car> findByBrand(Brand brand);

    @Query("FROM Car c WHERE c.modelYear > :modelYear")
    public List<Car> findAboveYear (int modelYear);
}
