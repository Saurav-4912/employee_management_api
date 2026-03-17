package com.employee.repository;

import com.employee.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee;

    @BeforeEach
    void setUp() {
        employee = Employee.builder()
                .name("John Doe")
                .email("john.doe@example.com")
                .department("IT")
                .position("Developer")
                .salary(new BigDecimal("60000.00"))
                .dateOfJoining(LocalDate.now())
                .build();
    }

    @Test
    void testSaveEmployee() {
        Employee savedEmployee = employeeRepository.save(employee);

        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isGreaterThan(0);
    }

    @Test
    void testFindById() {
        Employee savedEmployee = employeeRepository.save(employee);

        Optional<Employee> foundEmployee = employeeRepository.findById(savedEmployee.getId());

        assertThat(foundEmployee).isPresent();
        assertThat(foundEmployee.get().getName()).isEqualTo("John Doe");
    }

    @Test
    void testFindByEmail() {
        employeeRepository.save(employee);

        Optional<Employee> foundEmployee = employeeRepository.findByEmail("john.doe@example.com");

        assertThat(foundEmployee).isPresent();
        assertThat(foundEmployee.get().getDepartment()).isEqualTo("IT");
    }

    @Test
    void testDeleteEmployee() {
        Employee savedEmployee = employeeRepository.save(employee);
        employeeRepository.deleteById(savedEmployee.getId());

        Optional<Employee> deletedEmployee = employeeRepository.findById(savedEmployee.getId());

        assertThat(deletedEmployee).isEmpty();
    }
}
