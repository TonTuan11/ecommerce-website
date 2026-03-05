package com.tihuz.indentity_service.repository;

import com.tihuz.indentity_service.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

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
