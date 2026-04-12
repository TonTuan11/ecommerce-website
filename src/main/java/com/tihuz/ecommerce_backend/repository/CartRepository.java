package com.tihuz.ecommerce_backend.repository;

import com.tihuz.ecommerce_backend.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart,Long> {

    Optional<Cart> findByUserId( String userId);

//    @Query("""
//    select c from Cart c
//    left join fetch c.items i
//    left join fetch i.product
//    where c.user.id = :userId
//""")
//    Optional<Cart> findByUserIdWithItems(String userId);
}
