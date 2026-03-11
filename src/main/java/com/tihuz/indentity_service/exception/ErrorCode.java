package com.tihuz.indentity_service.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)

public enum ErrorCode  {


    // Trong enum JVM tự động new object, nên khi gọi là gọi object tĩnh ( static final object)

    // Mỗi hằng enum là một singleton object có dữ liệu riêng.
    // instance ( đối tượng tĩnh)
    INVALID_KEY ("Invalid message key",1001,HttpStatus.BAD_REQUEST),
    UNCATEGORIZED_EXCEPTION("uncategorized exception",4444, HttpStatus.INTERNAL_SERVER_ERROR),
    USER_EXISTED("User existed",1002,HttpStatus.BAD_REQUEST),
    FAIL_PASSWORD("Password fail",2002,HttpStatus.BAD_REQUEST),

    // Transactionrequest
    TRANSACTION_EXCEPTION("transaction exception",55555, HttpStatus.INTERNAL_SERVER_ERROR),


    USERNAME_INVALID3("Username invalid",1003,HttpStatus.BAD_REQUEST),
    USERNAME_INVALID("Username must be at least {min} character",1003,HttpStatus.BAD_REQUEST),
    USERNAME_INVALID1("Username must be at least {max} character",1003,HttpStatus.BAD_REQUEST),

    INVALID_PASSWORD("Password must be at least {min} character",1004,HttpStatus.BAD_REQUEST),
    USER_NOTEXISTED("User notexisted",1005,HttpStatus.NOT_FOUND),
    UNAUTHENTICATED("unauthenticated",1006,HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED("You do not have permission",1007,HttpStatus.FORBIDDEN),
    INVALID_DOB("Your age must be at least {min}",1008,HttpStatus.BAD_REQUEST),
    DOB_NOTNULL("DOB must not be null",1009,HttpStatus.BAD_REQUEST),
    ROLE_NOTEXISTED("ROLE not existed",1010,HttpStatus.BAD_REQUEST),


    // Permiisson
    PERMISSION_NOTEXISTED("Permisson not existed",1010,HttpStatus.NOT_FOUND),


    // Category

    CATE_EXISTED("Category existed",1011,HttpStatus.BAD_REQUEST),
    CATE_NOTEXISTED("Category notexisted",1012,HttpStatus.NOT_FOUND),
    CATE_NAME("Category Name Invalid",1013,HttpStatus.BAD_REQUEST),
    CATE_NOTNULL("Name must not be null",1014,HttpStatus.BAD_REQUEST),
    CATE_INVALID("Name must be at least {min} character",1015,HttpStatus.BAD_REQUEST),
    CATE_INVALID2("Name up to {max} character",1016,HttpStatus.BAD_REQUEST),
    CATE_INVALID3("Name cannot be all numbers ",1017,HttpStatus.BAD_REQUEST),
    CATE_PARENT_NOTEXISTED("Category parendId notexisted",1012,HttpStatus.NOT_FOUND),
    CATE_PARENT_INVALID("Category parendId invalid",1012,HttpStatus.BAD_REQUEST),
    CATE_HAS_CHILD("Category has children",1012,HttpStatus.BAD_REQUEST),




    //SLUG
    SLUG_EXISTED("Slug existed",1011,HttpStatus.BAD_REQUEST),





    //Brand
    BRAND_EXISTED("Category existed",1018,HttpStatus.BAD_REQUEST),
    BRAND_NOTEXISTED("Category notexisted",1019,HttpStatus.NOT_FOUND),
    BRAND_NAME("Category Name Invalid",1020,HttpStatus.BAD_REQUEST),
    BRAND_NOTNULL("Name must not be null",1021,HttpStatus.BAD_REQUEST),
    BRAND_INVALID("Name must be at least {min} character",1022,HttpStatus.BAD_REQUEST),
    BRAND_INVALID2("Name up to {max} character",1023,HttpStatus.BAD_REQUEST),
    BRAND_INVALID3("Name cannot be all numbers ",1024,HttpStatus.BAD_REQUEST),



    // PRODUCT

    PRODUCT_EXISTED("Product existed",1025,HttpStatus.BAD_REQUEST),
    PRODUCT_NOTEXISTED("Product notexisted",1026,HttpStatus.NOT_FOUND),
    PRODUCT_NAME("Product Name Invalid",1027,HttpStatus.BAD_REQUEST),
    PRODUCT_NOTNULL("Name must not be null",1028,HttpStatus.BAD_REQUEST),
    PRODUCT_INVALID("Name must be at least {min} character",1029,HttpStatus.BAD_REQUEST),
    PRODUCT_INVALID2("Name up to {max} character",1030,HttpStatus.BAD_REQUEST),
    PRODUCT_INVALID3("Name cannot be all numbers ",1031,HttpStatus.BAD_REQUEST),

    IMAGE_NOT_FOUND("image not found ",1444,HttpStatus.BAD_REQUEST),
    OUT_OF_STOCK( "Product is out of stock",3001,HttpStatus.BAD_REQUEST),



    // CART
    INVALID_QUANTITY("Quantity must be greater than zero",3002,HttpStatus.BAD_REQUEST),
    ITEM_NOT_FOUND("item not found",3003,HttpStatus.NOT_FOUND),
    CART_NOT_FOUND("cart not found",3004,HttpStatus.NOT_FOUND),


    //ORDER
    CART_EMPTY("Cart is empty",4001,HttpStatus.BAD_REQUEST)


    ;

    // các instance giờ là một object ErrorCode

    int code;
    String message;
    HttpStatusCode statusCode;


    //constructor
     ErrorCode(String message, int code, HttpStatusCode statusCode) {
        this.message = message;
        this.code = code;
        this.statusCode=statusCode;
    }



}
