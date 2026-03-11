package com.tihuz.ecommerce_backend.dto.request;
import com.tihuz.ecommerce_backend.validator.DobCostraint;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.LocalDate;

// class này dùng để nhận request từ client khi tạo user (tránh thao tác trực tiếp trên entity)
@Data
//annotation tạo constructor
@NoArgsConstructor
@AllArgsConstructor
//annotation tạo object nhanh hơn
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE) // set cho các thuộc tính là private
public class UserCreationRequest {

    @Size(min =4, max = 255,  message = "USERNAME_INVALID")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "USERNAME_INVALID3")
   // @Size(max =255, message = "USERNAME_INVALID1")
    String username;
    @Size(min =6,message = "INVALID_PASSWORD")
     String password;
    String email;
     String firstname;
     String lastname;

    @NotNull(message = "DOB_NOTNULL")
    @DobCostraint(min = 16,message = "INVALID_DOB")
    LocalDate dob;


}
