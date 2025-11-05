package com.tom.first.vehicle.request;

import com.tom.first.vehicle.model.enums.Type;

public record VehicleResponse (String brand, String model, String color, String licensePlate, Type type) {

}