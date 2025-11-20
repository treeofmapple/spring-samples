package com.tom.rabbitmq.realtime.logic;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.tom.rabbitmq.realtime.model.Vehicle;
import com.tom.rabbitmq.realtime.repository.VehicleRepository;

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

	public Vehicle updatePosition(Vehicle vehicle) {
		vehicle.setLatitude(Double.valueOf(dataUtil.generateLatitude()));
		vehicle.setLongitude(Double.valueOf(dataUtil.generateLongitude()));
		vehicle.setSpeedKmH(dataUtil.getRandomDouble(0, 200));
		vehicle.setFuelPercent(dataUtil.getRandomInteger(0, 100));
		vehicle.setEngineTemperature(dataUtil.getRandomDouble(0, 70));
		vehicle.setOdometerKm(dataUtil.getRandomDouble(0, 1000000));
		return vehicle;
	}

	private Vehicle genVehicle() {
		Vehicle vehicle = new Vehicle();
		vehicle.setPlateNumber(dataUtil.generateCarLicensePlate());
		vehicle.setModel(dataUtil.generateCarModel());
		return vehicle;
	}

}
