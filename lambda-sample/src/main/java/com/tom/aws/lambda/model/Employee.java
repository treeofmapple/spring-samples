package com.tom.aws.lambda.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;

@Data
@AllArgsConstructor
@NoArgsConstructor
@DynamoDbBean
public class Employee {

	private String id;
	private String name;
	private String email;
	private String jobTitle;
	private String phone;
	private String imageUrl;
	private String employeeCode;
	
    @DynamoDbPartitionKey
    public String getId() {
        return this.id;
    }
	
	@DynamoDbSecondaryPartitionKey(indexNames = "email-index")
	public String getEmail() {
		return this.email;
	}
	
    @DynamoDbSecondaryPartitionKey(indexNames = "job-title-index")
    public String getJobTitle() {
        return this.jobTitle;
    }
}
