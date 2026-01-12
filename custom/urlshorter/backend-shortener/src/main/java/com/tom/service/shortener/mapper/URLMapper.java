package com.tom.service.shortener.mapper;

import java.time.ZonedDateTime;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Service;

import com.tom.service.shortener.dto.URLComplete;
import com.tom.service.shortener.dto.URLResponse;
import com.tom.service.shortener.model.URL;

@Service
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface URLMapper {

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "accessCount", ignore = true)
	URL buildAtributes(String shortUrl, String originalUrl, ZonedDateTime expirationTime);

	URLResponse toResponse(URL url);

	URLComplete toResponseComplete(URL url);

}
