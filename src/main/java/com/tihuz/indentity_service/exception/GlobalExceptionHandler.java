package com.tihuz.indentity_service.exception;



import com.tihuz.indentity_service.dto.response.ApiResponse;
import jakarta.persistence.TransactionRequiredException;
import jakarta.validation.ConstraintViolation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;
import java.util.Objects;

@Slf4j // để dùng log

//Đánh dấu class này là nơi xử lý lỗi toàn cục cho tất cả controller trong ứng dụng.
//Khi controller nào đó xảy ra lỗi (exception), Spring sẽ tìm class này để xử lý
@ControllerAdvice
public class GlobalExceptionHandler {

    // Placeholder dùng trong message để thay giá trị thực từ annotation
    private static final String MIN_ATTRIBUTE="min";

    private static final String MAX_ATTRIBUTE="max";

    // khai báo để Spring biết đây là method xử lý lỗi kiểu RuntimeException
    @ExceptionHandler(value = RuntimeException.class)        //safety net
    ResponseEntity<ApiResponse> handlerRuntimeException(RuntimeException exception) // khi có lỗi RuntimeException  method này được gọi
    {
        log.error("Unexpected error: ", exception);
//        ApiResponse apiResponse=new ApiResponse();
//        apiResponse.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
//        apiResponse.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage());


        ErrorCode errorCode=ErrorCode.UNCATEGORIZED_EXCEPTION;

        ApiResponse apiResponse= ApiResponse.builder()
                .code((errorCode.getCode()))
                .message(errorCode.getMessage())
                .build();

        return  ResponseEntity
                .status(errorCode.getStatusCode())
                .body(apiResponse);


    }



    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handlerAppException(AppException exception) // khi có lỗi nghiệp vụ (business error) method này được gọi
    {
        ErrorCode errorCode=exception.getErrorCode();


//        ApiResponse apiResponse=new ApiResponse();
//        apiResponse.setCode(errorCode.getCode());
//        apiResponse.setMessage(errorCode.getMessage());


        ApiResponse apiResponse= ApiResponse.builder()
                .code((errorCode.getCode()))
                .message(errorCode.getMessage())
                .result(ErrorContext.get())
                .build();

        return  ResponseEntity
                .status(errorCode.getStatusCode())
                .body(apiResponse);

    }



    //khi có lỗi validate @Valid / @NotNull / @Size … ở DTO request thì Spring sẽ quăng ra MethodArgumentNotValidException
    @ExceptionHandler(value =  MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handlingValidation(MethodArgumentNotValidException validException)
    {

        // Lấy default message của field lỗi đầu tiên, dùng làm key để map sang ErrorCode
        String enumkey = validException.getFieldError().getDefaultMessage();  // (cái  đặt trong @NotBlank/@Size)

        // Mặc định nếu không tìm thấy key
        ErrorCode errorCode=ErrorCode.INVALID_KEY;

        // Map chứa attributes của annotation (ví dụ min, max...)
        Map<String ,Object> attributes=null;

        try {
            // Ép enumKey thành ErrorCode (nếu tồn tại)
                errorCode=   ErrorCode.valueOf(enumkey);

            // Lấy lỗi đầu tiên, unwrap thành ConstraintViolation để truy cập attribute
                var constrainViolation = validException
                        .getBindingResult() //lấy kết quả binding (toàn bộ lỗi).
                        .getAllErrors() //trả về list ObjectError (mỗi cái là một lỗi)
                        .get(0) //lấy lỗi đầu tiên để xử lý (nếu có nhiều lỗi thì chỉ xử lý một).
                        .unwrap(ConstraintViolation.class);    //lấy ra object chi tiết của constraint (ràng buộc)

            // Lấy attributes từ constraint (ví dụ {"min":5,
            //                                      "message":"..."})
                attributes= constrainViolation.getConstraintDescriptor().getAttributes();

               log.info(attributes.toString());
        }catch (IllegalArgumentException e)
        {

            // Bắt lỗi nếu enumKey không hợp lệ
            // Giữ nguyên errorCode là INVALID_KEY

        }

//        ApiResponse apiResponse=new ApiResponse();
//        apiResponse.setCode(errorCode.getCode());
//        apiResponse.setMessage(errorCode.getMessage());


        // Trả về response 400 với code và message
        return ResponseEntity.badRequest()
                .body(ApiResponse.builder()
                        .code(errorCode.getCode())
                        .message(Objects.nonNull(attributes) ?
                                // Nếu có attributes, thay giá trị trong message
                                mapAtribute(errorCode.getMessage(),attributes)
                                : errorCode.getMessage())
                        .build()

        );
    }


    private String mapAtribute(String message, Map<String,Object> attributes)
    {

        // Lấy giá trị min từ attributes, ép về String
//        String minValue=String.valueOf( attributes.get(MIN_ATTRIBUTE));
//        String maxValue=String.valueOf( attributes.get(MAX_ATTRIBUTE));

        // Thay {min} trong message bằng giá trị thực
//        return message.replace("{"+MIN_ATTRIBUTE+"}", minValue);
//        return message.replace("{"+MAX_ATTRIBUTE+"}", maxValue);

        String result = message;
        if (attributes.containsKey(MIN_ATTRIBUTE)) {
            result = result.replace("{" + MIN_ATTRIBUTE + "}", String.valueOf(attributes.get(MIN_ATTRIBUTE)));
        }

        if (attributes.containsKey(MAX_ATTRIBUTE)) {
            result = result.replace("{" + MAX_ATTRIBUTE + "}", String.valueOf(attributes.get(MAX_ATTRIBUTE)));
        }

        return result;

    }





    //xử lý lỗi 403 Forbidden t
    //không có quyền (role/authority) để truy cập tài nguyên.
    @ExceptionHandler(value = TransactionRequiredException.class)
    ResponseEntity<ApiResponse> handlingAccessDeniedException(TransactionRequiredException exception)
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


    // bắt lỗi @Transactional
//    @ExceptionHandler(value = InvalidDataAccessApiUsageException.class)
//    ResponseEntity<ApiResponse> handingTransactionRequest(InvalidDataAccessApiUsageException exception)
//    {
//        ErrorCode errorCode=ErrorCode.TRANSACTION_EXCEPTION;
//
//        return ResponseEntity.status(errorCode.getStatusCode())
//                .body(
//                        ApiResponse.builder()
//                        .code(errorCode.getCode())
//                        .message(errorCode.getMessage())
//                        .build()
//                );
//    }


}
