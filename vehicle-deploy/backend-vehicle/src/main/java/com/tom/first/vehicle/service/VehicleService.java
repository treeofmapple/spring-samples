package com.tom.first.vehicle.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tom.first.vehicle.common.SecurityUtils;
import com.tom.first.vehicle.dto.BrandResponse;
import com.tom.first.vehicle.dto.ModelResponse;
import com.tom.first.vehicle.dto.PageVehicleResponse;
import com.tom.first.vehicle.dto.VehicleRequest;
import com.tom.first.vehicle.dto.VehicleResponse;
import com.tom.first.vehicle.dto.VehicleUpdate;
import com.tom.first.vehicle.mapper.VehicleMapper;
import com.tom.first.vehicle.model.Vehicle;
import com.tom.first.vehicle.model.enums.Type;
import com.tom.first.vehicle.repository.VehicleRepository;
import com.tom.first.vehicle.repository.VehicleSpecification;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class VehicleService {

	@Value("${application.page.size:10}")
	private int PAGE_SIZE;
	
	private final VehicleRepository vehicleRepository;
	private final VehicleMapper mapper;
	private final VehicleUtils vehicleUtils;
	private final BrandUtils brandUtils;
	private final ModelUtils modelUtils;
	private final SecurityUtils securityUtils;
	
	@Transactional(readOnly = true)
	public PageVehicleResponse searchVehicleByParams(int page, String plate, String brand, String model, String color) {
		Specification<Vehicle> spec = VehicleSpecification.findByCriteria(plate, brand, model, color);
		log.info("IP: {}, is finding params: {}", securityUtils.getRequestingClientIp());
		
		Pageable pageable = PageRequest.of(page, PAGE_SIZE);
		Page<Vehicle> vehicle = vehicleRepository.findAll(spec, pageable); 
		return mapper.toResponse(vehicle);
	}

	@Transactional(readOnly = true)
	public VehicleResponse findById(long query) {
		var vehicle = vehicleUtils.findById(query);
		return mapper.toResponse(vehicle);
	}
	
	@Transactional(readOnly = true)
	public BrandResponse findBrandById(long query) {
		var brand = brandUtils.findById(query);
		return mapper.toResponse(brand);
	}

	@Transactional(readOnly = true)
	public ModelResponse findModelById(long query) {
		var model = modelUtils.findById(query);
		return mapper.toResponse(model);
	}
	
	@Transactional
	public VehicleResponse createVehicle(VehicleRequest request) {
		log.info("IP: {}, is creating vehicle: {}", securityUtils.getRequestingClientIp(), request.licensePlate());
		
		vehicleUtils.ensureArePlateUnique(request.licensePlate());
		
		var brand = brandUtils.findOrCreate(request.brand());
		var model = modelUtils.findOrCreate(request.model(), brand);
		
		Type type = Type.fromString(request.type());
		var vehicle = mapper.build(request);
		
		vehicle.setBrand(brand);
		vehicle.setModel(model);
		vehicle.setType(type);
		
		var vehicleSaved = vehicleRepository.save(vehicle);
		return mapper.toResponse(vehicleSaved);
	}

	@Transactional
	public VehicleResponse updateVehicle(long query, VehicleUpdate request) {
		log.info("IP: {}, is updating vehicle: {}", securityUtils.getRequestingClientIp(), query);
		
		var olderVehicle = vehicleUtils.findById(query);
		
	    vehicleUtils.checkIfPlateIsTaken(olderVehicle, request.licensePlate());
		var brand = brandUtils.checkIfBrandIsTaken(olderVehicle.getBrand(), request.brand());
		var model = modelUtils.checkIfModelIsTaken(olderVehicle.getModel(), brand, request.model());
		
		var vehicle = mapper.update(olderVehicle, request);
		
		vehicle.setBrand(brand);
		vehicle.setModel(model);
		
		var vehicleSaved = vehicleRepository.save(vehicle);
		return mapper.toResponse(vehicleSaved);
	}

	@Transactional
	public void deleteVehicle(long query) {
		var vehicle = vehicleUtils.findById(query);
		log.info("IP: {}, is deleting vehicle: {}", securityUtils.getRequestingClientIp(), query);
		vehicleRepository.delete(vehicle);
	}

}