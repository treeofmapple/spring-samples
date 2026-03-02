package com.tom.benchmark.client.mapper;

import java.util.List;
import java.util.UUID;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import com.tom.benchmark.client.dto.ClientRequest;
import com.tom.benchmark.client.dto.ClientResponse;
import com.tom.benchmark.client.dto.ClientUpdate;
import com.tom.benchmark.client.dto.PageClientResponse;
import com.tom.benchmark.client.model.Client;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, imports = { UUID.class,
		java.time.ZonedDateTime.class })
public interface ClientMapper {

	@Mapping(target = "id", expression = "java(UUID.randomUUID())")
	@Mapping(target = "createdAt", expression = "java(java.time.ZonedDateTime.now())")
	Client build(ClientRequest request);
	
	ClientResponse toResponse(Client client);
	
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	void update(@MappingTarget Client client, ClientUpdate request);
	
	default PageClientResponse toResponse(List<Client> list, Integer page, Integer size) {
		List<ClientResponse> content = list.stream().map(this::toResponse).toList();
		return new PageClientResponse(content, page, size, 0, (long) list.size());
	}
}
