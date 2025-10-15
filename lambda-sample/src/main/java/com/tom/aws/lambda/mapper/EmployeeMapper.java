package com.tom.aws.lambda.mapper;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import com.tom.aws.lambda.dto.EmployeeRequest;
import com.tom.aws.lambda.dto.EmployeeResponse;
import com.tom.aws.lambda.dto.EmployeeUpdate;
import com.tom.aws.lambda.dto.PageEmployeeResponse;
import com.tom.aws.lambda.model.Employee;

import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EmployeeMapper {

	Employee build(EmployeeRequest request);
	Employee update(@MappingTarget Employee employee, EmployeeUpdate request);
	EmployeeResponse toResponse(Employee employee);

	List<EmployeeResponse> toResponseList(List<Employee> employee);
	
	default PageEmployeeResponse toResponse(Page<Employee> page) {
		if (page == null) {
			return null;
		}
		List<EmployeeResponse> content = toResponseList(page.items());
		
        String nextToken = null;
        if (page.lastEvaluatedKey() != null && !page.lastEvaluatedKey().isEmpty()) {
            nextToken = serializeKey(page.lastEvaluatedKey());
        }
		
        return new PageEmployeeResponse(content, nextToken);
		
	}
	
    private String serializeKey(Map<String, AttributeValue> key) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(new java.util.HashMap<>(key));
            return Base64.getUrlEncoder().encodeToString(baos.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException("Could not serialize pagination key", e);
        }
    }
	
}
