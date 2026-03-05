package com.tihuz.indentity_service.validator;




import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


// áp dụng lên FIELD
@Target({ FIELD})

@Retention(RUNTIME)
@Constraint(validatedBy = {DobValidator.class})

public  @interface DobCostraint {

    String message() ;

    int min();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
