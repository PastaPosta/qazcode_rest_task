package com.qazcode.rest.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="customers")
public class  Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "name must not be blank")
    private String name;

    @Email(message = "email is not correct")
    @Column(nullable = false, unique = true)
    @NotBlank(message = "email must not be blank")
    private String email;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
