package com.tihuz.ecommerce_backend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
//This annotation hides fields that are null in the JSON response.
@JsonInclude(JsonInclude.Include.NON_NULL)

//ApiResponse is a generic wrapper that can return any data type (object, list, or pagination).
public class ApiResponse <T> //generic type
{

     @Builder.Default
     int code=1000;

     String message;

     T result;

}
