package com.tihuz.ecommerce_backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryCreationRequest {

    @NotBlank(message = "CATE_NOTNULL")
    @Size(max = 100, message = "CATE_INVALID2")
    @Size(min = 3, message = "CATE_INVALID")
    String name;

    Long parentId;

}


//“Sao không gửi object cho tiện? mà chỉ gửi mỗi parentId?”

//        “UI có thể hiển thị object, nhưng API chỉ nên nhận ID.
//Như vậy API gọn, dễ validate, tránh overposting và backend giữ quyền kiểm soát dữ liệu.”
