package com.rodrigovalest.ms_costumer.models.entities;

import com.rodrigovalest.ms_costumer.models.enums.GenderEnum;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "tb_customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "cpf", nullable = false, length = 14, unique = true)
    private String cpf;

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private GenderEnum gender;

    @Column(name = "birthdate", nullable = false)
    private LocalDate birthdate;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "points")
    private Long points = 0L;

    @Column(name = "url_photo")
    private String urlPhoto;
}
