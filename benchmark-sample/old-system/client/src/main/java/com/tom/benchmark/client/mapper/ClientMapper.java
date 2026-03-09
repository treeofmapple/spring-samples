package com.tom.benchmark.client.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.tom.benchmark.client.dto.ClientRequest;
import com.tom.benchmark.client.dto.ClientResponse;
import com.tom.benchmark.client.model.Client;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ClientMapper {

	ClientMapper INSTANCE = Mappers.getMapper(ClientMapper.class);
	
	Client toClient(ClientRequest request);
	
	ClientResponse toResponse(Client client);
	
	// @Mapping(target = "id", ignore = true)
	// void updateClientFromRequest(ClientRequest request, @MappingTarget Client client);
	
}
