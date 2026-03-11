package com.tihuz.ecommerce_backend.repository;

import com.tihuz.ecommerce_backend.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductImageRepository extends JpaRepository<ProductImage,Long> {

    List<ProductImage> findByProductId(Long productId);


    Optional<ProductImage> findByIdAndProductId(Long id, Long productId);
   // Optional<ProductImage> findById(Long id, Long productId);



    // JPQL mặc định là SELECT
    // nên muốn update delete phải có  @Modifying
    @Modifying
    @Query("update ProductImage i set i.isThumbnail = false where i.product.id = :productId")  //JPQL
    void clearThumbnail(Long productId);


    void  deleteByProductId( Long productId);
}
