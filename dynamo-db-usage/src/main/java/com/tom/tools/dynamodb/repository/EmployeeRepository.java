package com.tom.tools.dynamodb.repository;

import org.springframework.stereotype.Repository;

import com.tom.tools.dynamodb.model.Employee;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.UpdateItemEnhancedRequest;

@Repository
public class EmployeeRepository {

	private final DynamoDbTable<Employee> employeeTable;

	public EmployeeRepository(DynamoDbEnhancedClient enhancedClient) {
		this.employeeTable = enhancedClient.table("employee", TableSchema.fromBean(Employee.class));
	}
	
	public void save(Employee employee) {
		employeeTable.putItem(employee);
	}

	public Employee getEmployeeById(String employeeId) {
		Key key = Key.builder().partitionValue(employeeId).build();
		return employeeTable.getItem(key);
	}

	public void delete(String employeeId) {
		Key key = Key.builder().partitionValue(employeeId).build();
		employeeTable.deleteItem(key);
	}

	@SuppressWarnings("deprecation")
	public Employee update(String employeeId, Employee employee) {
		
		employee.setEmployeeId(employeeId);
		
		Expression condition = Expression.builder()
				.expression("attribute_exists(employeeId)")
				.build();
		
		UpdateItemEnhancedRequest<Employee> request = UpdateItemEnhancedRequest.builder(Employee.class)
				.item(employee)
                .ignoreNulls(false) 
				.conditionExpression(condition)
				.build();

		return employeeTable.updateItem(request);
	}

}
