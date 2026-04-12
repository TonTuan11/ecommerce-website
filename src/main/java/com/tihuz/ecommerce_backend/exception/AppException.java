package com.tihuz.ecommerce_backend.exception;

import lombok.Getter;
import lombok.Setter;

//app exception  throw error Runtime
//custom exception
@Getter
@Setter
public class AppException extends RuntimeException{

    private ErrorCode errorCode;

    public AppException(ErrorCode errorCode)
    {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
