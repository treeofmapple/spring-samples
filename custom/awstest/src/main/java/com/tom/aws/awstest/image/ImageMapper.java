package com.tom.aws.awstest.image;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.tom.aws.awstest.image.dto.ImageResponse;
import com.tom.aws.awstest.image.dto.PageImageResponse;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ImageMapper {

	ImageResponse toResponse(Image image);
	
    @Mapping(source = "file.originalFilename", target = "name")
    @Mapping(source = "file.contentType", target = "contentType")
    @Mapping(source = "file.size", target = "size")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Image mergeData(@MappingTarget Image Image, MultipartFile file, String objectKey, String objectUrl);

    List<ImageResponse> toResponseList(List<Image> images);
    
    default PageImageResponse toResponse(Page<Image> page) {
    	if(page == null) {
    		return null;
    	}
		List<ImageResponse> content = toResponseList(page.getContent());
		return new PageImageResponse(
				content,
				page.getNumber(),
				page.getSize(),
				page.getTotalElements()
			);
    }
    
}
