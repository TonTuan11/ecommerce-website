package com.tihuz.ecommerce_backend.dto.request;

import com.tihuz.ecommerce_backend.validator.DobConstraint;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class UserUpdateRequest
{
     String password;
     String firstname;
     String lastname;

    @DobConstraint(min = 18,message = "INVALID_DOB")
     LocalDate dob;

     List<String> roles;

}
