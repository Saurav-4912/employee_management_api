package com.employee.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "employees")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Employee{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Column(nullable = false, length = 100)
    private String name;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @NotBlank(message = "Department is required")
    @Column(nullable = false, length = 100)
    private String department;

    @NotBlank(message = "Position is required")
    @Column(nullable = false, length = 100)
    private String position;

    @NotNull(message = "Salary is required")
    @DecimalMin(value = "0.0", message = "Salary must be positive")
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal salary;

    @NotNull(message = "Joining date is required")
    @Column(nullable = false)
    private LocalDate dateOfJoining;
}