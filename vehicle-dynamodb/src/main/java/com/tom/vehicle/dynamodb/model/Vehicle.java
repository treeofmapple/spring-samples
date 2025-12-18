package com.tom.vehicle.dynamodb.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;

@DynamoDbBean
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle {

    private String id;
    private String brand;
    private String model;
    private String color;
    private String plate;

    @DynamoDbPartitionKey
    @DynamoDbAttribute("id")
    public String getId() {
        return id;
    }

    @DynamoDbAttribute("brand")
    public String getBrand() {
        return brand;
    }

    @DynamoDbAttribute("model")
    public String getModel() {
        return model;
    }

    @DynamoDbAttribute("color")
    public String getColor() {
        return color;
    }

    @DynamoDbSecondaryPartitionKey(indexNames = "plate-index")
    @DynamoDbAttribute("plate")
    public String getPlate() {
        return plate;
    }

}
