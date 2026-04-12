package com.tihuz.ecommerce_backend.repository;

import com.tihuz.ecommerce_backend.entity.Product;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product>
       { //Extend JpaSpecificationExecutor<> to use findAll(Specification, Pageable).

    boolean existsByName(String name);
    boolean existsBySlug (String slug);

    @EntityGraph(attributePaths = "images")
    Optional<Product> findBySlug(String slug);
    Optional<Product> findById(Long id);

}
