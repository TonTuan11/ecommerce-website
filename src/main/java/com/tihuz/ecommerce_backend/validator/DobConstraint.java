package com.tihuz.ecommerce_backend.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ FIELD}) //using for FIELD
@Retention(RUNTIME) //Keep annotation at runtime for validation.
@Constraint(validatedBy = {DobValidator.class}) // Use DobValidator for validation.
public  @interface DobConstraint
{
    String message() ;

    int min();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
