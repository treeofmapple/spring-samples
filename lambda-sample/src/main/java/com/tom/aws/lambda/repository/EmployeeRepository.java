package com.tom.aws.lambda.repository;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.tom.aws.lambda.model.Employee;

import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

@Repository
@RequiredArgsConstructor
public class EmployeeRepository {

	private final DynamoDbTemplate dynamoDbTemplate;
	
    public Employee save(Employee employee) {
        dynamoDbTemplate.save(employee);
        return employee;
    }
	
    public Optional<Employee> findById(String employeeId) {
        Key key = Key.builder().partitionValue(employeeId).build();
        return Optional.ofNullable(dynamoDbTemplate.load(key, Employee.class));
    }

    public void deleteById(String employeeId) {
        Key key = Key.builder().partitionValue(employeeId).build();
        dynamoDbTemplate.delete(key, Employee.class);
    }
    
    public boolean existsById(String employeeId) {
        return findById(employeeId).isPresent();
    }

	public Optional<Employee> findByEmail(String email) {
		QueryConditional queryConditional = QueryConditional.keyEqualTo(Key.builder().partitionValue(email).build());

		QueryEnhancedRequest queryRequest = QueryEnhancedRequest.builder().queryConditional(queryConditional).build();

		var results = dynamoDbTemplate.query(queryRequest, Employee.class, "email-index");

		if (results.iterator().hasNext()) {
			Page<Employee> firstPage = results.iterator().next();
			return firstPage.items().stream().findFirst();
		}

		return Optional.empty();

	}
	
    public boolean existsByEmployeeCode(String employeeCode) {
        Expression filterExpression = Expression.builder()
                .expression("employeeCode = :codeVal")
                .expressionValues(Map.of(":codeVal", AttributeValue.builder().s(employeeCode).build()))
                .build();
        
        ScanEnhancedRequest request = ScanEnhancedRequest.builder()
                .filterExpression(filterExpression)
                .limit(1)
                .build();
                
        return dynamoDbTemplate.scan(request, Employee.class).items().iterator().hasNext();
    }
    
    public Page<Employee> searchEmployees(String name, String jobTitle, String pageToken, int pageSize) {
        Map<String, AttributeValue> expressionValues = new HashMap<>();
        List<String> filterConditions = new ArrayList<>();

        if (name != null && !name.trim().isEmpty()) {
            filterConditions.add("contains(name, :nameVal)");
            expressionValues.put(":nameVal", AttributeValue.builder().s(name).build());
        }

        if (jobTitle != null && !jobTitle.trim().isEmpty()) {
            filterConditions.add("contains(jobTitle, :jobTitleVal)");
            expressionValues.put(":jobTitleVal", AttributeValue.builder().s(jobTitle).build());
        }
        
        ScanEnhancedRequest.Builder requestBuilder = ScanEnhancedRequest.builder().limit(pageSize);

        if (!filterConditions.isEmpty()) {
            String filterExpressionString = String.join(" AND ", filterConditions);
            Expression filterExpression = Expression.builder()
                    .expression(filterExpressionString)
                    .expressionValues(expressionValues)
                    .build();
            requestBuilder.filterExpression(filterExpression);
        }
        
        if (pageToken != null && !pageToken.isEmpty()) {
            requestBuilder.exclusiveStartKey(deserializeKey(pageToken));
        }
                
        return dynamoDbTemplate.scan(requestBuilder.build(), Employee.class).iterator().next();
    }

    private Map<String, AttributeValue> deserializeKey(String token) {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(Base64.getUrlDecoder().decode(token));
             ObjectInputStream ois = new ObjectInputStream(bais)) {
            @SuppressWarnings("unchecked")
            Map<String, AttributeValue> key = (Map<String, AttributeValue>) ois.readObject();
            return key;
        } catch (Exception e) {
            throw new RuntimeException("Could not deserialize pagination key", e);
        }
    }
    
}
