package com.tom.aws.awstest.image;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.tom.aws.awstest.bucket.AwsFunctions;
import com.tom.aws.awstest.bucket.ResourceUtils;
import com.tom.aws.awstest.common.SecurityUtils;
import com.tom.aws.awstest.image.dto.ImageExport;
import com.tom.aws.awstest.image.dto.ImageResponse;
import com.tom.aws.awstest.image.dto.PageImageResponse;
import com.tom.aws.awstest.logic.GenData;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class ImageService {

	private List<String> ALLOWED_IMAGE_TYPES = Stream.of("image/jpeg", "image/png", "image/gif", "image/webp")
			.collect(Collectors.toCollection(ArrayList::new));

	@Value("${application.page.size:10}")
	private int PAGE_SIZE;

	@Value("${application.file.size:20}")
	private long FILE_SIZE;

	private final ImageRepository repository;
	private final ImageMapper mapper;
	private final ImageUtils imageUtils;
	private final AwsFunctions functions;
	private final SecurityUtils securityUtils;
	private final ResourceUtils resourceUtils;
	private final GenData dataGeneration;

	@Transactional(readOnly = true)
	public PageImageResponse searchObjectByParams(int page, String query) {
		Pageable pageable = PageRequest.of(page, PAGE_SIZE);
		var images = imageUtils.findByIdOrNamePageable(query, pageable);
		return mapper.toResponse(images);
	}

	@Transactional(readOnly = true)
	public ImageResponse searchObjectById(long query) {
		var image = imageUtils.findImageById(query);
		return mapper.toResponse(image);
	}

	@Transactional
	public ImageResponse uploadObject(MultipartFile file) {
		String userIp = securityUtils.getRequestingClientIp();
		log.info("IP {} is uploading an object", userIp);

		if (file.isEmpty()) {
			throw new IllegalArgumentException("Cannot upload an empty file.");
		}

		long maxSizeInBytes = FILE_SIZE * 1024 * 1024;
		if (file.getSize() > maxSizeInBytes) {
			throw new IllegalArgumentException("File size exceeds the 20MB limit.");
		}

		if (!ALLOWED_IMAGE_TYPES.contains(file.getContentType())) {
			throw new IllegalArgumentException("Invalid file type. Only PNG and JPG images are allowed.");
		}

		String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
		String generatedKey = "images/" + UUID.randomUUID() + "-" + originalFilename;
		String s3Url = functions.uploadObject(generatedKey, file);

		var image = new Image();
		var merged = mapper.mergeData(image, file, generatedKey, s3Url);
		var imageSaved = repository.save(merged);
		return mapper.toResponse(imageSaved);
	}

	@Transactional
	public ImageExport downloadObject(String query) {
		String userIp = securityUtils.getRequestingClientIp();
		log.info("IP {} is removing object: {}", userIp, query);
		var image = imageUtils.findByIdOrName(query);

		var imageData = functions.downloadObject(image.getObjectKey());

		String zipFileName = image.getName() + ".zip";
		byte[] zipBytes = resourceUtils.resourceToBytes(imageData, image.getName());

		Resource resource = new ByteArrayResource(zipBytes);
		return new ImageExport(resource, zipFileName, zipBytes.length);
	}

	@Transactional
	public ImageResponse renameObject(String query, String newName) {
		String userIp = securityUtils.getRequestingClientIp();
		log.info("IP {} is renaming object: {}, to {}", userIp, query, newName);

		var image = imageUtils.findByIdOrName(query);
		String newKey = functions.renameObject(image, newName);
		image.setObjectKey(newKey);

		repository.save(image);
		return mapper.toResponse(image);
	}

	@Transactional
	public void removeObject(String query) {
		String userIp = securityUtils.getRequestingClientIp();
		log.info("IP {} is removing object", userIp);

		var image = imageUtils.findByIdOrName(query);
		functions.deleteObject(image.getObjectKey());
		repository.delete(image);
		log.info("Successfully deleted image for note: {}", query);
	}

	public PageImageResponse generateImages(int quantity) {
		String userIp = securityUtils.getRequestingClientIp();
		log.info("IP {} is generating {}: images", userIp, quantity);

		dataGeneration.processGenerateImages(quantity);

		Pageable pageable = PageRequest.of(0, PAGE_SIZE);
		Page<Image> images = repository.findMostRecent(pageable);
		log.info("Images finished generation");
		return mapper.toResponse(images);
	}

}
