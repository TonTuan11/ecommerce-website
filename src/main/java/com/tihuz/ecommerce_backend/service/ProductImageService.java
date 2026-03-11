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

import java.io.IOException;
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


    public List<ProductImageResponse> upload(
            Long productId,
            List<MultipartFile> files
    )
    {

        Product product=productRepository.findById(productId)
                .orElseThrow(()->new AppException(ErrorCode.PRODUCT_NOTEXISTED));

        List<ProductImage> images=new ArrayList<>();

        for( MultipartFile file:files)
        {
            if( file.isEmpty()) continue; //upload form nhưng chưa chọn file
                                            //Browser vẫn gửi part rỗng
                                            //Tránh tạo file rác

            String fileName= UUID.randomUUID()+"_"+file.getOriginalFilename(); //getOriginalFilename :tên file

            Path uploadPath= Paths.get("uploads/products/"+productId);      // Đường dẫn

            try
            {

                Files.createDirectories(uploadPath);      // tạo folder nếu chưa tồn tại
                Path filePath=uploadPath.resolve(fileName);  // ghép đường dẫn với tên file  vd: uploads/products/7/uuid_gojo.webp
                Files.write(filePath,file.getBytes());  //Lấy byte[] từ file upload ghi xuống disk
                System.out.println("toAbsolutePath:"+filePath.toAbsolutePath());  // in ra Path full
            }
             catch (IOException e) {
                throw new RuntimeException("upload failed");
            }

            ProductImage image=ProductImage.builder()
                    .url("/uploads/products/" + productId + "/" + fileName)
                    .isThumbnail(false)
                    .product(product)
                    .build();
            images.add(image);

        }

        // save() chỉ nhận 1 entity
        productImageRepository.saveAll(images);  // nhiều entity


        // trả về ProductImageResponse
        return images.stream()
                .map(img -> ProductImageResponse.builder()
                        .id(img.getId())
                        .url(img.getUrl())
                        .isThumbnail(img.getIsThumbnail())
                        .build())
                .toList();
    }



    // cần transactional vì luồng 2 bước
    // 1. set các image khác false
    // 2. set 1 image tre
    @Transactional
    public void setThumbnail(Long productId, Long imageId) {

        ProductImage image = productImageRepository
                .findByIdAndProductId(imageId, productId)
                .orElseThrow(() -> new AppException(ErrorCode.IMAGE_NOT_FOUND));

        // 1. Tắt toàn bộ thumbnail cũ của product
        productImageRepository.clearThumbnail(productId);

        // 2. Set thumbnail mới
        image.setIsThumbnail(true);
    }


}
