package app.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int year;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonIgnoreProperties("cars")
    @JsonBackReference
    private Brand brand;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "car_owner")
    private List<Proprietario> proprietarios;
}
