package com.tihuz.indentity_service.dto.response;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
// annotation này khi json có field nào =null thì ẩn
@JsonInclude(JsonInclude.Include.NON_NULL)


//ApiResponse là một response wrapper.
//Nó dùng Generic để có thể chứa nhiều kiểu dữ liệu khác nhau như một object,
//một danh sách, hoặc dữ liệu phân trang, tùy theo từng API.
public class ApiResponse <T> { //generic type

     @Builder.Default
     int code=1000;

     String message;

     T result; // kiểu dữ liệu sẽ do người dùng class quyết định

}
