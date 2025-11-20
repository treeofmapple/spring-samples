package com.tom.front.full.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.tom.front.full.model.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {

	Optional<Employee> findByName(String name);

	boolean existsByName(String name);

	boolean existsByNameAndIdNot(String name, long id);

	boolean existsByEmployeeCode(String employeeCode);

}
