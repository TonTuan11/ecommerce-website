package com.tihuz.ecommerce_backend.service;

import com.tihuz.ecommerce_backend.dto.request.ProductCreationRequest;
import com.tihuz.ecommerce_backend.dto.request.ProductFilterRequest;
import com.tihuz.ecommerce_backend.dto.request.ProductImageRequest;
import com.tihuz.ecommerce_backend.dto.request.ProductUpdateRequest;
import com.tihuz.ecommerce_backend.dto.response.ProductImageResponse;
import com.tihuz.ecommerce_backend.dto.response.ProductResponse;
import com.tihuz.ecommerce_backend.entity.Brand;
import com.tihuz.ecommerce_backend.entity.Category;
import com.tihuz.ecommerce_backend.entity.Product;
import com.tihuz.ecommerce_backend.entity.ProductImage;
import com.tihuz.ecommerce_backend.enums.ProductStatus;
import com.tihuz.ecommerce_backend.exception.AppException;
import com.tihuz.ecommerce_backend.exception.ErrorCode;
import com.tihuz.ecommerce_backend.mapper.ProductMapper;
import com.tihuz.ecommerce_backend.repository.BrandRepository;
import com.tihuz.ecommerce_backend.repository.CategoryRepository;
import com.tihuz.ecommerce_backend.repository.ProductRepository;
import com.tihuz.ecommerce_backend.specification.ProductSpecification;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.text.Normalizer;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductService
{
    ProductRepository productRepository;
    ProductMapper productMapper;
    CategoryRepository categoryRepository;
    CategoryService categoryService;
    BrandRepository brandRepository;

    public ProductResponse create(ProductCreationRequest request)
    {
        Category category= categoryRepository.findById(request.getCategoryId())
                .orElseThrow(()->new AppException(ErrorCode.CATE_NOTEXISTED));

        Brand brand=brandRepository.findById(request.getBrandId())
                .orElseThrow(()->new AppException(ErrorCode.BRAND_NOTEXISTED));

        if( category.getChildren()!= null && !category.getChildren().isEmpty())
        {
            throw  new AppException(ErrorCode.CATE_HAS_CHILD);
        }

        // check name
        String name=checkName( request.getName());
        if(productRepository.existsByName(name))
        {
            throw new AppException(ErrorCode.PRODUCT_EXISTED);
        }

        // to slug
        String slug=toSlug(name);

        // set filed
        Product product= new Product();
        product.setName(name);
        product.setSlug(slug);
        product.setPrice(request.getPrice());
        product.setPriceSale(request.getPriceSale());
        product.setQuantity(request.getQuantity());
        product.setCategory(category);
        product.setBrand(brand);
        product.setStatus(ProductStatus.ACTIVE);


        // check image
        if(request.getImages()!=null)
        {
            for (ProductImageRequest img: request.getImages())
            {
                ProductImage image = ProductImage.builder()
                                                 .url(img.getUrl())
                                                 .isThumbnail(img.getIsThumbnail())
                                                 .product(product)
                                                 .build();
                product.getImages().add(image);   // add image to list images (object graph in RAM)
            }
        }


         // URL of the thumbnail image
        String thumbnail = product.getImages()
                .stream()
                .filter(img -> Boolean.TRUE.equals(img.getIsThumbnail()))
                .map(ProductImage::getUrl)
                .findFirst()
                .orElse(null);

        // result thumbnail = "b.jpg";



        // Convert  List<ProductImage> -> List<ProductImageResponse>
        List<ProductImageResponse> images = product.getImages()
                                                    .stream()
                                                    .map(img -> ProductImageResponse.builder()
                                                            .id(img.getId())
                                                            .url(img.getUrl())
                                                            .isThumbnail(img.getIsThumbnail())
                                                            .build())
                .toList();

        // save repo
        Product saved=productRepository.save(product);


        // return response
        return ProductResponse.builder()
                .id(saved.getId())
                .name(saved.getName())
                .slug(saved.getSlug())
                .price(saved.getPrice())
                .quantity(saved.getQuantity())
                .categoryId(category.getId())
                .categoryName(category.getName())
                .brandId(brand.getId())
                .brandName(brand.getName())
                .thumbnail(thumbnail)
                .images(images)
                .status(saved.getStatus())
                .createdAt(saved.getCreatedAt())
                .updatedAt(saved.getUpdatedAt())
                .build();
    }


    public Page<ProductResponse> getAll(int page, int size)
    {

        Pageable pageable= PageRequest.of(page,size);
        return productRepository.findAll(pageable)
                .map(productMapper::toProductResponse);

    }

    public  ProductResponse getProductBySlug (String slug)
    {
        Product product= productRepository.findBySlug(slug)
                .orElseThrow(()->new AppException(ErrorCode.PRODUCT_NOTEXISTED));


        return productMapper.toProductResponse(product);

    }

    public  ProductResponse getProductById (Long productId)
    {
        Product product= productRepository.findById(productId)
                .orElseThrow(()->new AppException(ErrorCode.PRODUCT_NOTEXISTED));


        return productMapper.toProductResponse(product);

    }


    public ProductResponse updateProduct(String slug,ProductUpdateRequest request)
    {
        Product product= productRepository.findBySlug(slug)
                .orElseThrow(()->new AppException(ErrorCode.PRODUCT_NOTEXISTED));

        String name= checkName(request.getName());
        String newslug=toSlug(name);

        if (productRepository.existsByName(name) && !product.getName().equals(name) )
        {
            throw new AppException(ErrorCode.PRODUCT_EXISTED);
        }

        if( productRepository.existsBySlug(newslug) && !product.getSlug().equals(newslug))
        {
            throw new AppException(ErrorCode.PRODUCT_EXISTED);
        }


        // kiểm tra category có tồn tại không
        Category category= categoryRepository.findById(request.getCategoryId())
                .orElseThrow(()->new AppException(ErrorCode.CATE_NOTEXISTED));

        Brand brand= brandRepository.findById(request.getBrandId())
                .orElseThrow(()->new AppException(ErrorCode.BRAND_NOTEXISTED));

        if( category.getChildren()!=null && !category.getChildren().isEmpty())
        {
            throw  new AppException(ErrorCode.CATE_HAS_CHILD);
        }

          productMapper.updateProduct(product,request);
            product.setName(name);
            product.setSlug(newslug);
            product.setCategory(category);
            product.setBrand(brand);
            product.setQuantity(request.getQuantity());

         return productMapper.toProductResponse(productRepository.save(product));


    }



    public void deleteProduct(String slug)
    {
        Product product= productRepository.findBySlug(slug)
                .orElseThrow(()->new AppException(ErrorCode.PRODUCT_NOTEXISTED));

         productRepository.delete(product);
    }


    public Page<ProductResponse> filterProducts(ProductFilterRequest request, int page, int size, String sort )
    {
        Sort sortObj=Sort.unsorted();  // chưa sắp xếp

        if( sort!=null && !sort.isBlank())
        {
            String [] sortParams=sort.split(",");
            Sort.Direction direction= Sort.Direction.ASC;

            if( sortParams.length>1 && "desc".equalsIgnoreCase(sortParams[1]))
            {
                direction= Sort.Direction.DESC;
            }
            sortObj=Sort.by(direction,sortParams[0]);  // Sort.by(DESC,"price")
        }


//        Sort sortObj=parseSort(sort);

        Pageable pageable=PageRequest.of(page,size,sortObj);

        List<Long> categoryIds= null;

        if( request.getCategoryId() != null)
        {
            categoryIds=categoryService.getAllChildCategoryIds(request.getCategoryId());
        }

        Specification<Product> spec= ProductSpecification.filter(
                categoryIds,
                request.getKeyword(),
                request.getMinPrice(),
                request.getMaxPrice()

        );

        return productRepository.findAll(spec,pageable)
                .map(productMapper::toProductResponse);

    }



    // dùng trong các trường hợp:
    // muốn kiểm soát filed khi sort, tránh bừa bãi,
    //  dễ đọc, maintain
    // tránh sort vào filed không index
//    private Sort parseSort( String sort)
//    {
//        if(sort==null ||  sort.isBlank())
//        {
//            return Sort.by(Sort.Direction.DESC,"");
//
//        }
//        switch (sort)
//        {
//            case "price_asc":
//                return Sort.by(Sort.Direction.ASC,"price");
//            case "price_desc":
//                return Sort.by(Sort.Direction.DESC, "price");
//            case "newest":
//                return Sort.by(Sort.Direction.DESC, "createdAt");
//            default:
//                return Sort.by(Sort.Direction.DESC, "createdAt");
//
//        }
//
//    }




    public String checkName(String name) {
        name = name.trim();
        boolean allDigit = true;
        char prev = 0;
        
        for (int i = 0; i < name.length(); i++)
        {
            char c = name.charAt(i);

            if (!(Character.isLetter(c) || Character.isDigit(c) || c == ' ' || c == '-' || c == '_'))
            {
                throw new AppException(ErrorCode.PRODUCT_NAME);
            }

            if (!Character.isDigit(c))
            {
                allDigit = false;
            }

            if (c == '-' || c == '_')
            {
                if (i == 0 || i == name.length() - 1) { throw new AppException(ErrorCode.PRODUCT_NAME); } // throw chỉ là in lõi
                if (prev == '-' || prev == '_')   { throw new AppException(ErrorCode.PRODUCT_NAME); }
                if (prev == ' ')  { throw new AppException(ErrorCode.PRODUCT_NAME); }

                char next = name.charAt(i + 1);
                if (next == ' ')  { throw new AppException(ErrorCode.PRODUCT_NAME); }
            }
            if (c == ' ')
            {
                if (prev == ' ')   {throw new AppException(ErrorCode.PRODUCT_NAME);}
            }
            prev = c;
        }
        if (allDigit)         {throw new AppException(ErrorCode.PRODUCT_INVALID3);}
        return name;
    }



    // Hàm tạo slug tự động
    public static String toSlug(String input) {
        if (input == null) return "";
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        String slug = normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        slug = slug.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-");
        return slug;
    }

}
