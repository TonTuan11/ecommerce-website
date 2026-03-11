package com.tihuz.ecommerce_backend.dto.request;
import com.tihuz.ecommerce_backend.validator.DobCostraint;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.LocalDate;
import java.util.List;

// class này dùng để nhận request từ client khi tạo user (tránh thao tác trực tiếp trên entity)
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class UserUpdateRequest {
     String password;
     String firstname;
     String lastname;

    @DobCostraint(min = 18,message = "INVALID_DOB")
     LocalDate dob;
    List<String> roles;

}
