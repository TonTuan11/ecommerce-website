package com.tihuz.ecommerce_backend.service;

import com.tihuz.ecommerce_backend.dto.response.ProductImageResponse;
import com.tihuz.ecommerce_backend.entity.Product;
import com.tihuz.ecommerce_backend.entity.ProductImage;
import com.tihuz.ecommerce_backend.exception.AppException;
import com.tihuz.ecommerce_backend.exception.ErrorCode;
import com.tihuz.ecommerce_backend.repository.ProductImageRepository;
import com.tihuz.ecommerce_backend.repository.ProductRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class ProductImageService {

    ProductRepository productRepository;
    ProductImageRepository productImageRepository;

    public List<ProductImageResponse> upload( Long productId, List<MultipartFile> files )
    {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOTEXISTED));

        List<ProductImage> images = new ArrayList<>();

        for (MultipartFile file : files)
        {
            if (file.isEmpty()) continue;   // Skip empty files (browser may send empty parts in multipart requests)
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename(); //getOriginalFilename : filename
            Path uploadPath = Paths.get("uploads/products/" + productId);

            try
            {
                Files.createDirectories(uploadPath);      // Create directory if it does not exist
                Path filePath = uploadPath.resolve(fileName);  // file path + file name  vd: uploads/products/7/uuid_gojo.webp
                Files.write(filePath, file.getBytes());  // Write uploaded file bytes to disk
                System.out.println("toAbsolutePath:" + filePath.toAbsolutePath());  // Path full
            }
            catch (IOException e)
            {
                throw new RuntimeException("upload failed");
            }

            ProductImage image = ProductImage.builder()
                                             .url("/uploads/products/" + productId + "/" + fileName)
                                             .isThumbnail(false)
                                             .product(product)
                                             .build();
                                             images.add(image);
        }

            productImageRepository.saveAll(images);

            // return ProductImageResponse
            return images.stream()
                    .map(img -> ProductImageResponse.builder()
                                                                .id(img.getId())
                                                                .url(img.getUrl())
                                                                .isThumbnail(img.getIsThumbnail())
                                                                .build())
                                                                .toList();
    }



    // Requires a transaction due to the two-step process
    // 1. Set other images to false
    // 2. Set one image to true
    @Transactional
    public void setThumbnail(Long productId, Long imageId)
    {
        ProductImage image = productImageRepository
                .findByIdAndProductId(imageId, productId)
                .orElseThrow(() -> new AppException(ErrorCode.IMAGE_NOT_FOUND));

        // 1. Disable all existing product thumbnails
        productImageRepository.clearThumbnail(productId);

        // 2. Set thumbnail
        image.setIsThumbnail(true);
    }


    public List<ProductImageResponse> getImages(Long productId)
    {
        Product product = productRepository.findById(productId)
                                           .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOTEXISTED));

        return product.getImages().stream()
                                  .map (img -> ProductImageResponse.builder()
                                  .id(img.getId())
                                  .url(img.getUrl().startsWith("http") ? img.getUrl() : "http://localhost:8080" + img.getUrl())
                                  .isThumbnail(img.getIsThumbnail())
                                  .build()
                                  )
                                  .toList();
    }

    public InputStream getImageStream(Long productId, String fileName) throws IOException
    {
        Path filePath = Paths.get("uploads/products")
                             .resolve(String.valueOf(productId))
                             .resolve(fileName);

        if (!Files.exists(filePath))
        {
            throw new FileNotFoundException("File not found: " + fileName);
        }
        return Files.newInputStream(filePath);
    }

}
