package com.tihuz.indentity_service.mapper;

import com.tihuz.indentity_service.dto.request.CategoryCreationRequest;
import com.tihuz.indentity_service.dto.request.CategoryUpdateRequest;
import com.tihuz.indentity_service.dto.response.CategoryResponse;
import com.tihuz.indentity_service.entity.Category;
import org.mapstruct.*;

@Mapper(componentModel = "spring")

//“Mapper chỉ chuyển đổi dữ liệu,
//mọi xử lý quan hệ và business logic được đặt trong service layer.”
public interface CategoryMapper {


//    @Mapping(source = "name", target = "name")
////  //  @Mapping(source = "slug", target = "slug")
//    @Mapping(source = "position", target = "position")
//    @Mapping(source = "status", target = "status")





    Category toCate (CategoryCreationRequest request);


@Mapping(source = "parent.id", target = "parentId")
@Mapping(source = "parent.name", target = "parentName")
    CategoryResponse toCateResponse(Category category );



// annotation này dùng để nếu: request = null KHÔNG ghi đè lên entity Giữ nguyên giá trị cũ trong DB
    @BeanMapping(nullValuePropertyMappingStrategy =
            NullValuePropertyMappingStrategy.IGNORE)
    void updateCate(@MappingTarget Category category, CategoryUpdateRequest request);


}
