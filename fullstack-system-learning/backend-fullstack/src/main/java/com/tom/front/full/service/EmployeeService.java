package com.tom.front.full.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tom.front.full.common.SecurityUtils;
import com.tom.front.full.dto.EmployeeRequest;
import com.tom.front.full.dto.EmployeeResponse;
import com.tom.front.full.dto.EmployeeUpdate;
import com.tom.front.full.dto.PageEmployeeResponse;
import com.tom.front.full.mapper.EmployeeMapper;
import com.tom.front.full.model.Employee;
import com.tom.front.full.repository.EmployeeRepository;
import com.tom.front.full.repository.EmployeeSpecification;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmployeeService {

	@Value("${application.page.size:10}")
	private int PAGE_SIZE;

	private final EmployeeRepository employeeRepository;
	private final EmployeeMapper mapper;
	private final EmployeeUtils employeeUtils;
	private final SecurityUtils securityUtils;

	@Transactional(readOnly = true)
	public PageEmployeeResponse searchByParams(int page, String name, String email, String jobTitle,
			String employeeCode) {
		Specification<Employee> spec = EmployeeSpecification.findByCriteria(name, email, jobTitle, employeeCode);
		employeeUtils.logSearchParams(name, email, jobTitle, employeeCode);
		
		Pageable pageable = PageRequest.of(page, PAGE_SIZE);
		Page<Employee> employee = employeeRepository.findAll(spec, pageable);
		return mapper.toResponse(employee);
	}

	@Transactional(readOnly = true)
	public PageEmployeeResponse findEmployeesByPage(int page) {
		Pageable pageable = PageRequest.of(page, PAGE_SIZE);
		Page<Employee> employee = employeeRepository.findAll(pageable);
		return mapper.toResponse(employee);
	}
	
	@Transactional(readOnly = true)
	public EmployeeResponse findById(long query) {
		var employee = employeeUtils.findById(query);
		return mapper.toResponse(employee);
	}

	@Transactional
	public EmployeeResponse createEmployee(EmployeeRequest request) {
		securityUtils.logAction("creating employee", request.name());
		
		employeeUtils.ensureNameAreUnique(request.name());
		
		var employee = mapper.build(request);
		employee.setEmployeeCode(employeeUtils.generateEmployeeCode());
		
		var employeeSaved = employeeRepository.save(employee);
		return mapper.toResponse(employeeSaved);
	}

	@Transactional
	public EmployeeResponse updateEmployee(long query, EmployeeUpdate request) {
		securityUtils.logAction("updating employee", query);
		
		var olderEmployee = employeeUtils.findById(query);
		employeeUtils.checkIfNameIsTaken(olderEmployee, request.name());
		
		var employee = mapper.update(olderEmployee, request);
		
		var employeeSaved = employeeRepository.save(employee);
		return mapper.toResponse(employeeSaved);
	}

	@Transactional
	public EmployeeResponse generateNewEmployeeCode(long query) {
		securityUtils.logAction("generating new employee code", query);
		
		var employee = employeeUtils.findById(query);
		employee.setEmployeeCode(employeeUtils.generateEmployeeCode());
		
		var employeeSaved = employeeRepository.save(employee);
		return mapper.toResponse(employeeSaved);
	}
	
	@Transactional
	public void deleteById(long query) {
		var employee = employeeUtils.findById(query);
		securityUtils.logAction("deleting employee", employee.getName());
		employeeRepository.deleteById(employee.getId());
	}

}
