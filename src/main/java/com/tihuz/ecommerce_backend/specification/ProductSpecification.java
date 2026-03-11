package com.tihuz.ecommerce_backend.specification;


import com.tihuz.ecommerce_backend.entity.Product;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.criteria.Predicate;

public class ProductSpecification
{

public static Specification<Product> filter
        (
                List<Long> categoryIds,
                String keyword,
                BigDecimal minPrice,
                BigDecimal maxPrice
        )
    {
        return (root, query, cb) ->
        {
            List<Predicate> predicates= new ArrayList<>();  // điều kiện Where


            // điều kiện 1
            if( categoryIds !=null && !categoryIds.isEmpty())
            {
                predicates.add(root.get("category").get("id").in(categoryIds));
            }


            if( keyword!=null && !keyword.isBlank() )
            {
                String like = "%"+ keyword.toLowerCase()+ "%";

                predicates.add
                        (
                          cb.or(
                                cb.like(cb.lower(root.get("name")),like),
                                cb.like(cb.lower(root.get("slug")),like)

                               )
                        );
            }

            if(minPrice!=null)
            {
                predicates.add
                        (
                       cb.greaterThanOrEqualTo(root.get("price"),minPrice)  // price>=minPrice
                        );
            }


            if( maxPrice!=null)
            {
                predicates.add
                        (
                        cb.lessThanOrEqualTo(root.get("price"),maxPrice)  // price<=maxPrice
                        );
            }

            return cb.and(predicates.toArray(new Predicate[0]));

        };

      }

}
