package com.tihuz.ecommerce_backend.repository;

import com.tihuz.ecommerce_backend.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {

    boolean existsBySlug(String slug);

    boolean existsByName(String name);

    Optional<Category> findBySlug(String slug);

    void deleteBySlug( String slug);

    // dùng để thay cho lúc kiểm tra update
    // boolean existsByNameAndIdNot(String name, Long id);


    @Query("SELECT c FROM Category c")
    List<Category> findAllForTree();

    // Child category
    List<Category> findByParentId( Long parentId);


}
