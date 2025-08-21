package app.service;

import app.entity.Car;
import app.entity.Landlords;
import app.entity.Rental;
import app.repository.CarRepository;
import app.repository.LandLordsRepository;
import app.repository.RentalRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class RentalService {

    @Autowired
    private RentalRepository rentalRepository;
    @Autowired
    private CarRepository carRepository;
    @Autowired
    private LandLordsRepository landLordsRepository;

    public Rental findById(Long id) {
        return rentalRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Aluguel não encontrado com o ID: " + id));
    }

    public List<Rental> findAll() {
        return rentalRepository.findAll();
    }

    public List<Rental> findByCarId(Long carId) {
        if (!carRepository.existsById(carId)) {
            throw new EntityNotFoundException("Carro não encontrado com o ID: " + carId);
        }
        return rentalRepository.findByCar_Id(carId);
    }

    public List<Rental> findByLandlordId(Long landlordId) {
        if (!landLordsRepository.existsById(landlordId)) {
            throw new EntityNotFoundException("Locador não encontrado com o ID: " + landlordId);
        }
        return rentalRepository.findByLandlord_Id(landlordId);
    }

    public Rental create(Rental rentalRequest) {
        Car car = carRepository.findById(rentalRequest.getCar().getId())
                .orElseThrow(() -> new EntityNotFoundException("Carro não encontrado."));

        Landlords landlord = landLordsRepository.findById(rentalRequest.getLandlord().getId())
                .orElseThrow(() -> new EntityNotFoundException("Locador não encontrado."));

        List<Rental> overlappingRentals = rentalRepository.findOverlappingRentals(car.getId(), rentalRequest.getStartDate(), rentalRequest.getReturnDate());
        if (!overlappingRentals.isEmpty()) {
            throw new IllegalStateException("Este carro já está alugado para o período solicitado.");
        }

        double totalValue = calculateTotalValue(car, rentalRequest.getStartDate(), rentalRequest.getReturnDate());

        Rental newRental = new Rental();
        newRental.setCar(car);
        newRental.setLandlord(landlord);
        newRental.setStartDate(rentalRequest.getStartDate());
        newRental.setReturnDate(rentalRequest.getReturnDate());
        newRental.setTotalValue(totalValue);
        newRental.setStatus("ATIVO");

        return rentalRepository.save(newRental);
    }

    private double calculateTotalValue(Car car, LocalDateTime startDate, LocalDateTime endDate) {
        double baseDailyRate = car.getVehicleValue() * 0.003;
        double totalValue = 0;

        long totalDays = ChronoUnit.DAYS.between(startDate.toLocalDate(), endDate.toLocalDate());
        if (totalDays == 0) totalDays = 1;

        for (long i = 0; i < totalDays; i++) {
            LocalDateTime currentDay = startDate.plusDays(i);
            DayOfWeek dayOfWeek = currentDay.getDayOfWeek();

            if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
                totalValue += baseDailyRate * 1.20;
            } else {
                totalValue += baseDailyRate;
            }
        }
        return totalValue;
    }
}