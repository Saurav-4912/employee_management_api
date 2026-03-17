package com.employee.repository;

import com.employee.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

        boolean existsByEmail(String email);

        boolean existsByEmailAndIdNot(String email, Long id);

        Optional<Employee> findByEmail(String email);

        @Query("SELECT e FROM Employee e WHERE " +
                        "(:department IS NULL OR e.department = :department) AND " +
                        "(:position IS NULL OR e.position = :position) AND " +
                        "(:search IS NULL OR LOWER(e.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
                        "OR LOWER(e.email) LIKE LOWER(CONCAT('%', :search, '%')))")
        Page<Employee> findByFilters(
                        @Param("department") String department,
                        @Param("position") String position,
                        @Param("search") String search,
                        Pageable pageable);
}
