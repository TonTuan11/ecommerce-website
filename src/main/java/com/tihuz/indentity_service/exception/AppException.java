package com.tihuz.indentity_service.exception;


//appeception là để throw ra lỗi của Runtime
// kèm theo errocode mà mình đã khai báo tĩnh trong enum
// bằng cách tạo 1 constructor 1 tham số là errorCode và gán message vào để hiển thị lỗi cho rõ ràng

//custom exception
public class AppException extends RuntimeException{

    private ErrorCode errorCode;

    public AppException(ErrorCode errorCode) {
        super(errorCode.getMessage());  // truyền message cho RuntimeException
        this.errorCode = errorCode;   // giữ nguyên ErrorCode để dùng sau
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
