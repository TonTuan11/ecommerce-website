package com.tihuz.indentity_service.service;

import com.tihuz.indentity_service.dto.request.CategoryCreationRequest;
import com.tihuz.indentity_service.dto.request.CategoryUpdateRequest;
import com.tihuz.indentity_service.dto.response.CategoryResponse;
import com.tihuz.indentity_service.entity.Category;
import com.tihuz.indentity_service.exception.AppException;
import com.tihuz.indentity_service.exception.ErrorCode;
import com.tihuz.indentity_service.mapper.CategoryMapper;
import com.tihuz.indentity_service.repository.CategoryRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class CategoryService {


    CategoryRepository categoryRepository;
    CategoryMapper categoryMapper;


    public CategoryResponse createCate(CategoryCreationRequest request)
    {
        String name = checkName(request.getName());
        String slug= toSlug(name);


        // kiểm tra danh mục tồn tại chưa
        if( categoryRepository.existsByName(name))
            throw  new AppException((ErrorCode.CATE_EXISTED));




        // Mapper
        Category category=new Category();
        category.setName(name);
        category.setSlug(slug);

        Category parent = null;
        if( request.getParentId()!=null)
        {
             parent=categoryRepository.findById(request.getParentId())
                    .orElseThrow(()-> new AppException(ErrorCode.CATE_NOTEXISTED)) ;
            category.setParent(parent);
        }

        // không dùng mapper để map từ request sang entity nữa
        // mà map thủ công, vì có filed phức tạp: Category parent

        Category saved = categoryRepository.save(category);
        return CategoryResponse.builder()
                        .id(saved.getId())
                        .name(saved.getName())
                        .slug(saved.getSlug())
                        .parentId(parent!=null? parent.getId() : null)
                        .parentName(parent!=null? parent.getName() : null)
                        .status(saved.getStatus())
                        .createdAt(saved.getCreatedAt())
                        .updatedAt(saved.getUpdatedAt())
                        .build();

//        return categoryMapper.toCateResponse(
//                categoryRepository.save(category));

    }


    //@PreAuthorize("hasRole('ADMIN')")
    public Page<CategoryResponse> getAll(int page, int size, String sort)
    {
        String [] sortParams= sort.split(",");
        Sort.Direction direction=
                sortParams[1].equalsIgnoreCase("desc")
                ? Sort.Direction.DESC
                        :Sort.Direction.ASC;

        Pageable pageable= PageRequest.of(
                page,
                size,
                Sort.by(direction,sortParams[0])
        );


//        return categoryRepository.findAll()
//                .stream()
//                .map(categoryMapper:: toCateResponse)
//                .toList();

        return categoryRepository.findAll(pageable)
                .map(categoryMapper::toCateResponse); // map Category sang CategoryResponse
    }




    public CategoryResponse updateCate(String slug, CategoryUpdateRequest request)
    {


        Category category=categoryRepository.findBySlug(slug)
                .orElseThrow(()->new AppException( ErrorCode.CATE_NOTEXISTED));

        if(request.getName()!=null )
        {

            String name = checkName(request.getName());
            String slugSet = toSlug(name);


            //đang check trùng thủ công trước,
            //sau này có thể refactor sang query method để clean hơn.”
            if( categoryRepository.existsByName(name) && !category.getName().equals(name))
            {
                throw new AppException(ErrorCode.CATE_EXISTED);
            }

            if( categoryRepository.existsBySlug(slugSet) && !category.getSlug().equals(slugSet))
            {
                throw new AppException(ErrorCode.SLUG_EXISTED);

            }




//            Long parentId= request.getParentId();
//            if( parentId==null)
//            {
//                category.setParent(null);
//            }
//            else
//            {
//                if( parentId.equals(category.getId()))
//                {
//                    throw new AppException(ErrorCode.CATE_PARENT_INVALID);
//
//                }
//            }
//
//            Category parent=categoryRepository.findById(parentId)
//                    .orElseThrow(()-> new AppException(ErrorCode.CATE_PARENT_NOTEXISTED));



            if (request.getParentId() != null) {

                Long parentId = request.getParentId();

                if (parentId == 0 ) {
                    // client muốn xóa cha
                    category.setParent(null);
                } else {

                    if (parentId.equals(category.getId())) {
                        throw new AppException(ErrorCode.CATE_PARENT_INVALID);
                    }

                    Category parent = categoryRepository.findById(parentId)
                            .orElseThrow(() -> new AppException(ErrorCode.CATE_PARENT_NOTEXISTED));

                    if (checkParentIn(parent, category)) {
                        throw new AppException(ErrorCode.CATE_PARENT_INVALID);
                    }
                    category.setParent(parent);
                }
            }

//            if( !categoryRepository.existsById(request.getParentId()) && category.getId().equals(request.getParentId()))
//            {
//                throw new AppException(ErrorCode.CATE_PARENT_INVALID);
//            }

            category.setName(name);
            category.setSlug(slugSet);
          //  category.setParent(parent);

        }
        //Map
        categoryMapper.updateCate(category, request);
        return categoryMapper.toCateResponse(categoryRepository.save(category));

    }


    public void deleteCate( String slug)
    {
        Category category=categoryRepository.findBySlug(slug)
                . orElseThrow(()-> new AppException((ErrorCode.CATE_NOTEXISTED)) );

        if(!category.getChildren().isEmpty())
        {
            throw new AppException(ErrorCode.CATE_HAS_CHILD);
        }

        categoryRepository.delete(category);

    }


//    public CategoryResponse getCate(String slug)
//    {
//        return categoryMapper.toCateResponse(categoryRepository.findBySlug(slug)
//                .orElseThrow(()-> new AppException(ErrorCode.CATE_NOTEXISTED)));
//
//    }



    public CategoryResponse getCate(String slug) {

        Category category = categoryRepository.findBySlug(slug)
                .orElseThrow(() -> new AppException(ErrorCode.CATE_NOTEXISTED));

//        CategoryResponse res = categoryMapper.toCateResponse(category);
//
//        List<CategoryResponse> children = category.getChildren()
//                .stream()
//                .map(categoryMapper::toCateResponse)
//                .toList();
//
//        res.setChildren(children);

        return buildTree(category);
    }


    // lấy ra các con lồng nhau nhiều cấp
    private CategoryResponse buildTree( Category category)
    {
        CategoryResponse res = categoryMapper.toCateResponse(category);
        if( category.getChildren()!=null && !category.getChildren().isEmpty())
        {
//            List<CategoryResponse> children= category.getChildren()
//                    .stream()
//                    .map(this::buildTree)       //.map(child -> buildTree(child))
//                    .toList();
//            res.setChildren(children);

            List<CategoryResponse> children=new ArrayList<>();

            for( Category child:category.getChildren())
            {
                CategoryResponse childRes=buildTree(child);
                children.add(childRes);
            }
            res.setChildren(children);
        }
        return res;

    }




    public  List<CategoryResponse> getTree()
    {
        List<Category> categories= categoryRepository.findAllForTree();

        // map để tạo ra danh sách để tra cứu
        Map<Long, CategoryResponse> map= new HashMap<>(); //Tra cứu category theo ID
        for ( Category c:categories)
        {
            CategoryResponse valueCate= categoryMapper.toCateResponse(c);  // chuyển từ entity sang CateResponse
            valueCate.setChildren(new ArrayList<>());  //Chuẩn bị sẵn danh sách con vì nếu khai báo children = new ArrayList<>() trong DTO, mapper khiến nhiều node dùng chung 1 list → tree bị lỗi
            map.put(valueCate.getId(),valueCate);     // map có dạng : 1-> Electronics
        }

        // vòng lặp này dùng để gắn con vào cha
        List< CategoryResponse> roots= new ArrayList<>();  // đặt roots là node gốc

        for (Category c:categories)
        {
            CategoryResponse father=map.get(c.getId());  // lấy ra id rồi kiểm tra
            if (c. getParent()==null)                     // nếu không có cha thì add vào roots
            {
                roots.add(father);
            }
            else {
                CategoryResponse parent= map.get(c.getParent().getId());  // ngược lại, lấy id  cha ra
                parent.getChildren().add(father);
                // add vào biến được xem là  cha (trong filed children)
            }
        }
        return roots;  // trả về các node gốc đã có con
    }



    // Lấy các cây con của 1 cha
//    public List<Long> getAllChildCategoryIds(Long categoryId)
//    {
//        List<Long> ids= new ArrayList<>();
//        collectCategoryIds(categoryId,ids);
//        return ids;  // trả về bao gồm cha và tất cả con nó
//    }
//
//
//    public  void collectCategoryIds( Long categoryId, List<Long> ids)
//    {
//        ids.add(categoryId);
//        List<Category> children=categoryRepository.findByParentId(categoryId);
//        for (Category child: children)
//        {
//            collectCategoryIds(child.getId(),ids);
//        }
//    }


    public List<Long> getAllChildCategoryIds(Long rootId)
    {
        List<Long> result = new ArrayList<>();
        traverseCategoryTree(rootId,result );
        return result ;
    }


    public void traverseCategoryTree(Long currentId, List<Long> result)
    {
        result.add(currentId);

        List<Category> children=categoryRepository.findByParentId(currentId); // lấy ra con của currentId
        for (Category child:children)
        {
            traverseCategoryTree(child.getId(),result);  // đệ quy
        }

    }

    // Các hàm validation

    public String checkName(String name) {
        name = name.trim();
        boolean allDigit = true;
        char prev = 0;

        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);

            if (!(Character.isLetter(c) || Character.isDigit(c) || c == ' ' || c == '-' || c == '_')) {
                throw new AppException(ErrorCode.CATE_NAME);
            }

            if (!Character.isDigit(c))    { allDigit = false; }

            if (c == '-' || c == '_')
            {
                if (i == 0 || i == name.length() - 1) { throw new AppException(ErrorCode.CATE_NAME); }
                if (prev == '-' || prev == '_')   { throw new AppException(ErrorCode.CATE_NAME); }
                if (prev == ' ')  { throw new AppException(ErrorCode.CATE_NAME); }

                char next = name.charAt(i + 1);
                if (next == ' ')  { throw new AppException(ErrorCode.CATE_NAME); }
            }
            if (c == ' ')
            {
                if (prev == ' ')   {throw new AppException(ErrorCode.CATE_NAME);}
            }
            prev = c;
        }
        if (allDigit)         {throw new AppException(ErrorCode.CATE_INVALID3);}
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


    // kiểm tra parent nằm trong cây con không
    private boolean checkParentIn(Category newParent,Category current)
    {
       Category temp=newParent;

       while(temp!=null)
       {
           if( temp.getId().equals(current.getId()))
           {
               return true;
           }
           temp=temp.getParent();
       }

        return false;
    }




}
