package app.service;

import app.dto.CarSimpleDTO;
import app.dto.LandlordSimpleDTO;
import app.dto.RentalRequestDTO;
import app.dto.RentalResponseDTO;
import app.entity.Car;
import app.entity.Landlords;
import app.entity.Rental;
import app.repository.CarRepository;
import app.repository.LandLordsRepository;
import app.repository.RentalRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RentalService {

    @Autowired
    private RentalRepository rentalRepository;
    @Autowired
    private CarRepository carRepository;
    @Autowired
    private LandLordsRepository landlordsRepository;

    public Rental findById(Long id) {
        return rentalRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Aluguel não encontrado com o ID: " + id));
    }

    public List<RentalResponseDTO> findAll() {
        return rentalRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<RentalResponseDTO> findByCarId(Long carId) {
        if (!carRepository.existsById(carId)) {
            throw new EntityNotFoundException("Carro não encontrado com o ID: " + carId);
        }
        return rentalRepository.findByCar_Id(carId).stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<RentalResponseDTO> findByLandlordId(Long landlordId) {
        if (!landlordsRepository.existsById(landlordId)) {
            throw new EntityNotFoundException("Locador não encontrado com o ID: " + landlordId);
        }
        return rentalRepository.findByLandlord_Id(landlordId).stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public Rental create(RentalRequestDTO rentalDTO) {
        if (rentalDTO.car() == null || rentalDTO.car().getId() == null) {
            throw new IllegalArgumentException("O carro precisa ser informado.");
        }
        if (rentalDTO.landlord() == null || rentalDTO.landlord().getId() == null) {
            throw new IllegalArgumentException("O locador precisa ser informado.");
        }

        Car car = carRepository.findById(rentalDTO.car().getId())
                .orElseThrow(() -> new EntityNotFoundException("Carro não encontrado."));

        Landlords landlord = landlordsRepository.findById(rentalDTO.landlord().getId())
                .orElseThrow(() -> new EntityNotFoundException("Locador não encontrado."));

        List<Rental> overlappingRentals = rentalRepository.findOverlappingRentals(car.getId(), rentalDTO.startDate(), rentalDTO.returnDate());
        if (!overlappingRentals.isEmpty()) {
            throw new IllegalStateException("Este carro já está alugado para o período solicitado.");
        }

        double totalValue = calculateTotalValue(car, rentalDTO.startDate(), rentalDTO.returnDate());

        Rental newRental = new Rental();
        newRental.setCar(car);
        newRental.setLandlord(landlord);
        newRental.setStartDate(rentalDTO.startDate());
        newRental.setReturnDate(rentalDTO.returnDate());
        newRental.setTotalValue(totalValue);
        newRental.setStatus("ATIVO");

        return rentalRepository.save(newRental);
    }

    private double calculateTotalValue(Car car, LocalDateTime startDate, LocalDateTime endDate) {
        double baseDailyRate = car.getVehicleValue() * 0.003;
        double totalValue = 0;

        long totalDays = ChronoUnit.DAYS.between(startDate.toLocalDate(), endDate.toLocalDate());
        if (totalDays < 1) {
            totalDays = 1;
        }

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

    @Scheduled(cron = "0 0 * * * *")
    @Transactional
    public void updateExpiredRentalsStatus() {
        System.out.println("Executando tarefa agendada: Verificando aluguéis expirados...");

        List<Rental> activeRentals = rentalRepository.findByStatus("ATIVO");

        for (Rental rental : activeRentals) {
            if (rental.getReturnDate().isBefore(LocalDateTime.now())) {
                System.out.println("Aluguel ID " + rental.getId() + " expirou. Atualizando status para FINALIZADO.");
                rental.setStatus("FINALIZADO");
                rentalRepository.save(rental);
            }
        }
        System.out.println("Tarefa de verificação de aluguéis concluída.");
    }

    private RentalResponseDTO convertToDTO(Rental rental) {
        CarSimpleDTO carDTO = new CarSimpleDTO(rental.getCar().getId(), rental.getCar().getName());
        LandlordSimpleDTO landlordDTO = new LandlordSimpleDTO(rental.getLandlord().getId(), rental.getLandlord().getName());

        return new RentalResponseDTO(
                rental.getId(),
                rental.getStartDate(),
                rental.getReturnDate(),
                rental.getTotalValue(),
                rental.getStatus(),
                carDTO,
                landlordDTO
        );
    }



}