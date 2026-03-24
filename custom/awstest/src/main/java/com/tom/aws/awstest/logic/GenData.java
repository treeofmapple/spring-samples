package com.tom.aws.awstest.logic;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.tom.aws.awstest.bucket.AwsFunctions;
import com.tom.aws.awstest.image.Image;
import com.tom.aws.awstest.image.ImageMapper;
import com.tom.aws.awstest.image.ImageRepository;
import com.tom.aws.awstest.image.dto.ImageGenResponse;
import com.tom.aws.awstest.product.Product;
import com.tom.aws.awstest.product.ProductMapper;
import com.tom.aws.awstest.product.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
@RequiredArgsConstructor
public class GenData {

	private final ProductRepository productRepository;
	private final ImageRepository imageRepository;
	private final ProductMapper productMapper;
	private final ImageMapper imageMapper;
	private final AwsFunctions functions;
	private final GenerateDataUtil dataUtil;

    @Async
    @Transactional
	public void processGenerateProduct(int quantity) {
		List<Product> productsToSave = IntStream.range(0, quantity)
				.parallel()
				.mapToObj(i -> genProduct())
				.collect(Collectors.toList());
		productRepository.saveAll(productsToSave);
	}
    
    @Async
    @Transactional
	public void processGenerateImages(int quantity) {
    	 List<Image> imagesToSave = IntStream.range(0, quantity)
             .parallel()
             .mapToObj(i -> {
                 MultipartFile file = genImages();
                 if (file == null) {
                     return null;
                 }

                 String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
                 String generatedKey = "images/" + UUID.randomUUID() + "-" + originalFilename;
                 String s3Url = functions.uploadObject(generatedKey, file);

                 var image = new Image();
                 return imageMapper.mergeData(image, file, generatedKey, s3Url);
             })
             .filter(Objects::nonNull)
             .collect(Collectors.toList());

         if (!imagesToSave.isEmpty()) {
             imageRepository.saveAll(imagesToSave);
         }
	}
	
	private Product genProduct() {
		Product pro = new Product();

        String productName = dataUtil.generateProductName();
		int quantity = dataUtil.getRandomInt(5, 1000);
		BigDecimal price = dataUtil.getRandonBigDecimal(5, 250);
		String manufacturer = dataUtil.generateDeviceManufacturerName();
		boolean active = dataUtil.getRandomNumber(100) < 90; // 90 % chance

		var productUpdated = productMapper.mergeData(pro, productName, quantity, price, manufacturer, active);
		return productUpdated;
	}
	
	public MultipartFile genImages() {
		try {
			ImageGenResponse resource = dataUtil.generateAndDownloadImage();
            MultipartFile multipartFile = new ImageGenData(
                    resource.content(),
                    resource.fileName(),
                    resource.contentType()
            );
			return multipartFile; 

		} catch (RuntimeException e) {
			log.warn("Failed to generate and download image: " + e.getMessage());
			return null;
		}
	}
	
}
