package com.tom.first.vehicle.logic;

import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.tom.first.vehicle.model.Vehicle;
import com.tom.first.vehicle.model.enums.Type;
import com.tom.first.vehicle.repository.VehicleRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
@RequiredArgsConstructor
public class GenerateData {

	private final VehicleRepository vehicleRepository;
	private final GenerateDataUtil dataUtil;
	
    @Transactional
    public Vehicle processVehicle() {
    	var gen = genVehicle();
    	vehicleRepository.save(gen);
    	return gen;
    }
	
	private Vehicle genVehicle() {
		Vehicle vehicle = new Vehicle();
		vehicle.setBrand(dataUtil.generateCarBrand());
		vehicle.setModel(dataUtil.generateCarModel());
		vehicle.setColor(dataUtil.generateCarColor());
		vehicle.setPlate(dataUtil.generateCarLicensePlate());
		vehicle.setType(ThreadLocalRandom.current().nextBoolean() ? Type.CAR : Type.MOTORCYCLE);
		return vehicle;
	}
}
