package com.tihuz.ecommerce_backend.exception;



public class ErrorContext {

    private static final ThreadLocal<Object> DATA = new ThreadLocal<>();

    public static void set(Object data) {
        DATA.set(data);
    }

    public static Object get() {
        return DATA.get();
    }

    public static void clear() {
        DATA.remove();
    }
}
