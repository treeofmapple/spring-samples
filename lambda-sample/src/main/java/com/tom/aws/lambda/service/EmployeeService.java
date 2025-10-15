package com.tom.aws.lambda.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tom.aws.lambda.common.SecurityUtils;
import com.tom.aws.lambda.dto.EmployeeRequest;
import com.tom.aws.lambda.dto.EmployeeResponse;
import com.tom.aws.lambda.dto.EmployeeUpdate;
import com.tom.aws.lambda.dto.PageEmployeeResponse;
import com.tom.aws.lambda.mapper.EmployeeMapper;
import com.tom.aws.lambda.model.Employee;
import com.tom.aws.lambda.repository.EmployeeRepository;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;

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
	public PageEmployeeResponse searchByParams(String name, String jobTitle, String nextPageToken) {
		employeeUtils.logSearchParams(name, null, jobTitle, null);
	    Page<Employee> pageResult = employeeRepository.searchEmployees(name, jobTitle, nextPageToken, PAGE_SIZE);
	    		
		return mapper.toResponse(pageResult);
	}

	@Transactional(readOnly = true)
	public PageEmployeeResponse findEmployeesByPage(String nextPageToken) {
		return searchByParams(null, null, nextPageToken);
	}
	
	@Transactional(readOnly = true)
	public EmployeeResponse findById(String query) {
		var employee = employeeUtils.findById(query);
		return mapper.toResponse(employee);
	}

	@Transactional
	public EmployeeResponse createEmployee(EmployeeRequest request) {
		securityUtils.logAction("creating employee", request.name());
		
		employeeUtils.ensureEmailIsUnique(request.email());
		
		var employee = mapper.build(request);
		employee.setId(UUID.randomUUID().toString());
		employee.setEmployeeCode(employeeUtils.generateEmployeeCode());
		
		var employeeSaved = employeeRepository.save(employee);
		return mapper.toResponse(employeeSaved);
	}

	@Transactional
	public EmployeeResponse updateEmployee(String query, EmployeeUpdate request) {
		securityUtils.logAction("updating employee", query);
		
		var olderEmployee = employeeUtils.findById(query);
		employeeUtils.checkIfEmailIsTaken(olderEmployee, request.email());
		
		var employee = mapper.update(olderEmployee, request);
		
		var employeeSaved = employeeRepository.save(employee);
		return mapper.toResponse(employeeSaved);
	}

	@Transactional
	public EmployeeResponse generateNewEmployeeCode(String query) {
		securityUtils.logAction("generating new employee code", query);
		
		var employee = employeeUtils.findById(query);
		employee.setEmployeeCode(employeeUtils.generateEmployeeCode());
		
		var employeeSaved = employeeRepository.save(employee);
		return mapper.toResponse(employeeSaved);
	}
	
	@Transactional
	public void deleteByEmployeeCode(String query) {
		var employee = employeeUtils.findById(query);
		securityUtils.logAction("deleting employee", employee.getName());
		employeeRepository.deleteById(employee.getEmployeeCode());
	}

}
