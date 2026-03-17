package com.employee.service;

import com.employee.model.Employee;
import com.employee.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    private Employee employee;

    @BeforeEach
    void setUp() {
        employee = Employee.builder()
                .id(1L)
                .name("John Doe")
                .email("john.doe@example.com")
                .department("IT")
                .position("Developer")
                .salary(new BigDecimal("60000.00"))
                .dateOfJoining(LocalDate.now())
                .build();
    }

    @Test
    void testCreateEmployee_Success() {
        when(employeeRepository.existsByEmail(anyString())).thenReturn(false);
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        Employee createdEmployee = employeeService.createEmployee(employee);

        assertThat(createdEmployee).isNotNull();
        assertThat(createdEmployee.getName()).isEqualTo("John Doe");
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    void testCreateEmployee_EmailAlreadyExists() {
        when(employeeRepository.existsByEmail(anyString())).thenReturn(true);

        assertThrows(ResponseStatusException.class, () -> {
            employeeService.createEmployee(employee);
        });

        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    void testGetEmployeeById_Success() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        Employee foundEmployee = employeeService.getEmployeeById(1L);

        assertThat(foundEmployee).isNotNull();
        assertThat(foundEmployee.getId()).isEqualTo(1L);
    }

    @Test
    void testGetEmployeeById_NotFound() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> {
            employeeService.getEmployeeById(1L);
        });
    }

    @Test
    void testGetAllEmployees() {
        Page<Employee> employeePage = new PageImpl<>(Collections.singletonList(employee));

        when(employeeRepository.findByFilters(any(), any(), any(), any(Pageable.class))).thenReturn(employeePage);

        Page<Employee> result = employeeService.getAllEmployees(0, 10, "id", "asc", null, null, null);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        verify(employeeRepository, times(1)).findByFilters(any(), any(), any(), any(Pageable.class));
    }

    @Test
    void testUpdateEmployee_Success() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        Employee updatedEmployee = employeeService.updateEmployee(1L, employee);

        assertThat(updatedEmployee).isNotNull();
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    void testDeleteEmployee_Success() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        doNothing().when(employeeRepository).delete(any(Employee.class));

        employeeService.deleteEmployee(1L);

        verify(employeeRepository, times(1)).delete(any(Employee.class));
    }
}
