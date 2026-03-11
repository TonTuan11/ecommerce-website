package com.tihuz.ecommerce_backend.service;

import com.tihuz.ecommerce_backend.dto.request.BrandCreationRequest;
import com.tihuz.ecommerce_backend.dto.request.BrandUpdateRequest;
import com.tihuz.ecommerce_backend.dto.response.BrandResponse;
import com.tihuz.ecommerce_backend.entity.Brand;
import com.tihuz.ecommerce_backend.exception.AppException;
import com.tihuz.ecommerce_backend.exception.ErrorCode;
import com.tihuz.ecommerce_backend.mapper.BrandMapper;
import com.tihuz.ecommerce_backend.repository.BrandRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.List;


@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class BrandService {

    BrandRepository brandRepository;
    BrandMapper brandMapper;


    public BrandResponse createBrand(BrandCreationRequest request)
    {
        String name= checkName(request.getName());


        if ( brandRepository.existsByName(name))
        {
            throw new AppException(ErrorCode.BRAND_EXISTED);
        }

        String slug=toSlug(name);


        Brand brand =brandMapper.toBrand(request);
        brand.setName(name);
        brand.setSlug(slug);

      return brandMapper.toBrandResponse(brandRepository.save(brand));

    }


    public BrandResponse updateBrand(String name,BrandUpdateRequest request)
    {

        String namecheck=checkName(request.getName());
        Brand brand= brandRepository.findByName(name)
                .orElseThrow(()->new AppException(ErrorCode.BRAND_NOTEXISTED));

        brand.setSlug(toSlug(namecheck));

       brandMapper.updateBrand(brand,request);;
        brand.setName(namecheck);

       return brandMapper.toBrandResponse(brandRepository.save(brand));

    }


    public List<BrandResponse> getAll()
    {
        return brandRepository.findAll()
                .stream()
                .map(brandMapper :: toBrandResponse)
                .toList();

    }


    public BrandResponse getBrand( String name)
    {

        return brandMapper.toBrandResponse(brandRepository.findByName(name)
                .orElseThrow(()->new AppException(ErrorCode.BRAND_NOTEXISTED)));

    }


    public void deleteBrand(String name)
    {
        Brand brand= brandRepository.findByName(name)
                .orElseThrow(()-> new AppException(ErrorCode.BRAND_NOTEXISTED));

        brandRepository.deleteByName(name);



    }


//
//public String checkName(String name) {
//    name = name.trim();
//    StringBuilder sb = new StringBuilder();
//    boolean lastWasSpace = false;
//    boolean special = false;
//    boolean allDigit = true;
//
//    for (char c : name.toCharArray()) {
//        // kiểm tra ký tự hợp lệ
//        if (!(Character.isLetter(c) || Character.isDigit(c) || c == ' ' || c == '-' || c == '_')) {
//            throw new AppException(ErrorCode.BRAND_NAME);
//        }
//        if (!Character.isDigit(c)) {
//            allDigit = false;
//        }
//        if (c == '-' || c == '_') {
//            // bỏ khoảng trắng trước ký tự đặc biệt
//            if (sb.length() > 0 && sb.charAt(sb.length() - 1) == ' ')
//            {
//                sb.deleteCharAt(sb.length() - 1);
//            }
//            if (!special)
//            {
//                sb.append(c);
//                special = true;
//            }
//            lastWasSpace = false;
//        }
//        else if (Character.isWhitespace(c))
//        {
//            if (!special && !lastWasSpace)
//            {
//                sb.append(' ');
//                lastWasSpace = true;
//            }
//        }
//        else
//        {
//            sb.append(c);
//            special = false;
//            lastWasSpace = false;
//        }
//    }
//    if (allDigit) {
//        throw new AppException(ErrorCode.BRAND_INVALID3);
//    }
//    return sb.toString();
//}
//



    public String checkName(String name) {
        name = name.trim();
        boolean allDigit = true;
        char prev = 0;

        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);

            if (!(Character.isLetter(c) || Character.isDigit(c) || c == ' ' || c == '-' || c == '_')) {
                throw new AppException(ErrorCode.BRAND_NAME);
            }

            if (!Character.isDigit(c))    { allDigit = false; }

            if (c == '-' || c == '_')
            {
                if (i == 0 || i == name.length() - 1) { throw new AppException(ErrorCode.BRAND_NAME); }
                if (prev == '-' || prev == '_')   { throw new AppException(ErrorCode.BRAND_NAME); }
                if (prev == ' ')  { throw new AppException(ErrorCode.BRAND_NAME); }

                char next = name.charAt(i + 1);
                if (next == ' ')  { throw new AppException(ErrorCode.BRAND_NAME); }
            }
            if (c == ' ')
            {
                if (prev == ' ')   {throw new AppException(ErrorCode.BRAND_NAME);}
            }
            prev = c;
        }
        if (allDigit)         {throw new AppException(ErrorCode.BRAND_INVALID3);}
        return name;
    }


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
