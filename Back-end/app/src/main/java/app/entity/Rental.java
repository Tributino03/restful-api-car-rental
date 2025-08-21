package app.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "car_id")
    @JsonBackReference("car-rentals")
    private Car car;

    @ManyToOne
    @JoinColumn(name = "landlord_id")
    @JsonBackReference("landlord-rentals")
    private Landlords landlord;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "return_date")
    private LocalDateTime returnDate;

    @Column(name = "total_value")
    private Double totalValue;

    private String status;
}