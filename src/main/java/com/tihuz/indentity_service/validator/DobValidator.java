package com.tihuz.indentity_service.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;


// Đây là class implement từ interface ConstraintValidator<DobConstraint, LocalDate>
// Nó sẽ validate giá trị ngày sinh (dob) dựa trên annotation @DobConstraint
public class DobValidator implements ConstraintValidator<DobCostraint, LocalDate> {


    private int min; // tuổi tối thiểu (sẽ lấy từ annotation @DobConstraint)

    @Override
    public void initialize(DobCostraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);

        min=constraintAnnotation.min();
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext constraintValidatorContext) {

        if(Objects.isNull(value))
            return true;

        // Cách 1 (khuyên dùng): tính tuổi chuẩn theo năm-tháng-ngày
     //   int years = java.time.Period.between(value, LocalDate.now()).getYears();

        long years= ChronoUnit.YEARS.between(value, LocalDate.now());
        return years>=min;

    }





}
