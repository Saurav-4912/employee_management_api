package com.employee.service;

import com.employee.model.Employee;
import com.employee.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service layer for Employee CRUD operations with pagination and sorting.
 */
@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    /**
     * Creates a new employee record.
     */
    @Transactional
    public Employee createEmployee(Employee employee) {
        if (employeeRepository.existsByEmail(employee.getEmail())) {
            throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST, "Employee with email '" + employee.getEmail() + "' already exists.");
        }
        return employeeRepository.save(employee);
    }

    /**
     * Retrieves all employees with pagination and sorting support.
     */
    @Transactional(readOnly = true)
    public Page<Employee> getAllEmployees(
            int page, int size, String sortBy, String sortDirection,
            String department, String position, String search) {

        Sort sort = sortDirection.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        // Use null for empty filter strings so JPQL IS NULL check works
        String deptFilter = (department != null && !department.isBlank()) ? department : null;
        String posFilter = (position != null && !position.isBlank()) ? position : null;
        String searchFilter = (search != null && !search.isBlank()) ? search : null;

        return employeeRepository.findByFilters(deptFilter, posFilter, searchFilter, pageable);
    }

    /**
     * Retrieves a single employee by ID.
     */
    @Transactional(readOnly = true)
    public Employee getEmployeeById(Long id) {
        return findEmployeeOrThrow(id);
    }

    /**
     * Updates an existing employee record.
     */
    @Transactional
    public Employee updateEmployee(Long id, Employee request) {
        Employee employee = findEmployeeOrThrow(id);

        // Check for email conflict with another employee
        if (employeeRepository.existsByEmailAndIdNot(request.getEmail(), id)) {
            throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST, "Employee with email '" + request.getEmail() + "' already exists.");
        }

        employee.setName(request.getName());
        employee.setEmail(request.getEmail());
        employee.setDepartment(request.getDepartment());
        employee.setPosition(request.getPosition());
        employee.setSalary(request.getSalary());
        employee.setDateOfJoining(request.getDateOfJoining());

        return employeeRepository.save(employee);
    }

    /**
     * Deletes an employee by ID.
     */
    @Transactional
    public void deleteEmployee(Long id) {
        Employee employee = findEmployeeOrThrow(id);
        employeeRepository.delete(employee);
    }

    // ----------------------------- Private helpers -------------------------

    private Employee findEmployeeOrThrow(Long id) {
        return employeeRepository.findById(id)
                 .orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Employee not found with id: " + id));
    }
}
