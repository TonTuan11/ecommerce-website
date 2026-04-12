package com.tihuz.ecommerce_backend.repository;

import com.tihuz.ecommerce_backend.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage,Long> {

    List<ProductImage> findByProductId(Long productId);

    Optional<ProductImage> findByIdAndProductId(Long id, Long productId);
   // Optional<ProductImage> findById(Long id, Long productId);


    // Use @Modifying for update/delete.
    @Modifying
    @Query(
            value = "UPDATE product_image SET is_thumbnail = false WHERE product_id = :productId",
            nativeQuery = true   //Mark as native SQL.
    )
    void clearThumbnail(Long productId);

    void  deleteByProductId( Long productId);
}
