package com.tihuz.ecommerce_backend.exception;

import com.tihuz.ecommerce_backend.dto.response.ApiResponse;
import jakarta.persistence.TransactionRequiredException;
import jakarta.validation.ConstraintViolation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.Map;
import java.util.Objects;

@Slf4j
@ControllerAdvice    // Global exception handler for all controllers
public class GlobalExceptionHandler
{
    // Message placeholder for annotation values
    private static final String MIN_ATTRIBUTE="min";
    private static final String MAX_ATTRIBUTE="max";

    // RuntimeException
    @ExceptionHandler(value = RuntimeException.class)        //safety net
    ResponseEntity<ApiResponse> handlerRuntimeException(RuntimeException exception)
    {
        log.error("Unexpected error: ", exception);
        ErrorCode errorCode=ErrorCode.UNCATEGORIZED_EXCEPTION;
        ApiResponse apiResponse= ApiResponse.builder()
                                            .code((errorCode.getCode()))
                                            .message(errorCode.getMessage())
                                            .build();
        return  ResponseEntity
                .status(errorCode.getStatusCode())
                .body(apiResponse);
    }

    // Business logic error
    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handlerAppException(AppException exception)
    {
        ErrorCode errorCode=exception.getErrorCode();
        ApiResponse apiResponse= ApiResponse.builder()
                                            .code((errorCode.getCode()))
                                            .message(errorCode.getMessage())
                                            .result(ErrorContext.get())
                                            .build();

        return  ResponseEntity
                .status(errorCode.getStatusCode())
                .body(apiResponse);
    }


    //When validation fails (vd: @Valid, @NotNull, @Size) on a request DTO
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handlingValidation(MethodArgumentNotValidException validException)
    {
        // Get the message from the annotation (used as ErrorCode key)
        String enumKey = validException.getFieldError().getDefaultMessage();

        // Default error code if mapping fails
        ErrorCode errorCode = ErrorCode.INVALID_KEY;

        // Attributes from validation annotation (vd: min, max)
        Map<String, Object> attributes = null;

        try
        {
            // Map message key to ErrorCode enum
            errorCode = ErrorCode.valueOf(enumKey);

            // Get first validation error and extract constraint details
            var constraintViolation = validException
                    .getBindingResult()
                    .getAllErrors()
                    .get(0)
                    .unwrap(ConstraintViolation.class);

            // Get the attributes from the annotation (vd: {min=5, max=10})
            attributes = constraintViolation.getConstraintDescriptor().getAttributes();

        }
        catch (IllegalArgumentException e)
        {
            // Ignore if enumKey is invalid → use default errorCode
        }

        // Build response with optional attribute substitution
        return ResponseEntity.badRequest()
                             .body( ApiResponse.builder()
                                     .code(errorCode.getCode())
                                     .message(Objects.nonNull(attributes)
                                      ? mapAttribute(errorCode.getMessage(), attributes)
                                      : errorCode.getMessage())

                                    // .message(mapAttribute(errorCode.getMessage(),attributes))
                                      .build()
                                   );
    }

    private String mapAttribute(String message, Map<String,Object> attributes)
    {
        String result = message;
        if (attributes.containsKey(MIN_ATTRIBUTE))
        {
            result = result.replace("{" + MIN_ATTRIBUTE + "}", String.valueOf(attributes.get(MIN_ATTRIBUTE)));
        }

        if (attributes.containsKey(MAX_ATTRIBUTE))
        {
            result = result.replace("{" + MAX_ATTRIBUTE + "}", String.valueOf(attributes.get(MAX_ATTRIBUTE)));
        }
        return result;
    }

    //Transaction
    @ExceptionHandler({TransactionRequiredException.class, JpaSystemException.class, InvalidDataAccessApiUsageException.class})
    ResponseEntity<ApiResponse> handleTransactionException(RuntimeException ex)
    {
        ErrorCode errorCode = ErrorCode.TRANSACTION_EXCEPTION;
        log.error("Transaction exception", ex);
        return ResponseEntity.status(errorCode.getStatusCode())
                             .body(ApiResponse.builder()
                                    .code(errorCode.getCode())
                                    .message(errorCode.getMessage())
                                    .build());
    }

    // User not logged in
    @ExceptionHandler(value = AuthenticationException.class)
    ResponseEntity<ApiResponse> handlingAuthenticationException(AuthenticationException exception)
    {
        ErrorCode errorCode=ErrorCode.UNAUTHENTICATED;

        return  ResponseEntity.status(errorCode.getStatusCode())
                              .body(ApiResponse
                                    .builder()
                                    .code(errorCode.getCode())
                                    .message(errorCode.getMessage())
                                    .build()
                );
    }

    // Access denied
    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<ApiResponse> handlingAccessDeniedException(AccessDeniedException exception)
    {
        ErrorCode errorCode=ErrorCode.UNAUTHORIZED;
        return  ResponseEntity.status(errorCode.getStatusCode())
                              .body(ApiResponse
                                    .builder()
                                    .code(errorCode.getCode())
                                    .message(errorCode.getMessage())
                                    .build()
                            );

    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    ResponseEntity<ApiResponse> handlingHttpMessageNotReadableException(HttpMessageNotReadableException exception)
    {
        Throwable cause=exception.getCause();
        if (cause instanceof com.fasterxml.jackson.databind.exc.InvalidFormatException invalidFormat
                && invalidFormat.getTargetType().isEnum())
        {
            ErrorCode errorCode = ErrorCode.ENUM_INVALID;
            return ResponseEntity.status(errorCode.getStatusCode())
                    .body(ApiResponse.builder()
                            .code(errorCode.getCode())
                            .message(errorCode.getMessage())
                            .build());
        }

        else
        {
            ErrorCode errorCode = ErrorCode.BODY_INVALID;
            return ResponseEntity.status(errorCode.getStatusCode())
                    .body(ApiResponse.builder()
                            .code(errorCode.getCode())
                            .message(errorCode.getMessage())
                            .build());
        }

    }




//    @ExceptionHandler(DataIntegrityViolationException.class)
//    ResponseEntity<ApiResponse> handleDataIntegrity(DataIntegrityViolationException ex) {
//        ErrorCode errorCode = ErrorCode.BODY_INVALID;
//
//        return ResponseEntity.status(errorCode.getStatusCode())
//                .body(ApiResponse.builder()
//                        .code(errorCode.getCode())
//                        .message("Missing required field (DB constraint)")
//                        .build());
//    }


}
