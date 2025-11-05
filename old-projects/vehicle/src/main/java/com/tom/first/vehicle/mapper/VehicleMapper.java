package com.tom.first.vehicle.mapper;

import org.springframework.stereotype.Service;

import com.tom.first.vehicle.model.Vehicle;
import com.tom.first.vehicle.request.VehicleRequest;
import com.tom.first.vehicle.request.VehicleResponse;
import com.tom.first.vehicle.request.VehicleUpdate;

@Service
public class VehicleMapper {

	public Vehicle toVehicle(VehicleRequest request) {
		if(request == null) {
			return null;
		}
		
		return Vehicle.builder()
				.brand(request.brand())
				.model(request.model())
				.color(request.color())
				.plate(request.licensePlate())
				.type(request.getTypeEnum())
				.build();
	}
	
	public VehicleResponse fromVehicle(Vehicle response) {
		return new VehicleResponse(
				response.getBrand(),
				response.getModel(),
				response.getColor(),
				response.getPlate(),
				response.getType()
				);
	}
	
	public void mergeVehicle(Vehicle vehicle, VehicleUpdate request) {
		vehicle.setBrand(request.brand());
		vehicle.setModel(request.model());
		vehicle.setColor(request.color());
		vehicle.setPlate(request.licensePlate());
		vehicle.setType(request.type());
	}
	
}
