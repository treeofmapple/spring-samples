package com.tom.benchmark.monolith.client;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ClientMapper {

	Client toClient(ClientRequest request);
	
	ClientResponse toResponse(Client client);
	
}
